/*
 * Copyright (c) 2017, Bart Hanssens <bart.hanssens@fedict.be>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package be.belgif.vocab.ldf;

import be.belgif.vocab.App;
import be.belgif.vocab.helpers.QueryHelper;
import java.net.URI;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriBuilder;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.VOID;
import org.eclipse.rdf4j.query.BindingSet;

import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.MalformedQueryException;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.query.TupleQuery;

import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for querying the triple store using Linked Data Fragments.
 * 
 * @author Bart.Hanssens
 */
public class QueryHelperLDF {
	private final static Logger LOG = (Logger) LoggerFactory.getLogger(QueryHelperLDF.class);
	
	private final static ValueFactory F = SimpleValueFactory.getInstance();
	
	private final static String PREFIX = App.getPrefix();
	
	private final static IRI LDF_GRAPH = F.createIRI(App.getPrefixGraph() + "/ldf");
	private final static IRI LDF_SEARCH = F.createIRI(PREFIX + "_ldf#search");		
	
	private final static IRI LDF_MAP_S = F.createIRI(PREFIX + "_ldf#s");
	private final static IRI LDF_MAP_P = F.createIRI(PREFIX + "_ldf#p");
	private final static IRI LDF_MAP_O = F.createIRI(PREFIX + "_ldf#o");
	
	// Hydra mapping, does not change
	private final static Model HYDRA_MAPPING = new LinkedHashModel();
	static {
		HYDRA_MAPPING.add(LDF_MAP_S, Hydra.VARIABLE, F.createLiteral("s"), LDF_GRAPH);
		HYDRA_MAPPING.add(LDF_MAP_S, Hydra.PROPERTY, RDF.SUBJECT, LDF_GRAPH);

		HYDRA_MAPPING.add(LDF_MAP_P, Hydra.VARIABLE, F.createLiteral("p"), LDF_GRAPH);
		HYDRA_MAPPING.add(LDF_MAP_P, Hydra.PROPERTY, RDF.PREDICATE, LDF_GRAPH);
		
		HYDRA_MAPPING.add(LDF_MAP_O, Hydra.VARIABLE, F.createLiteral("o"), LDF_GRAPH);
		HYDRA_MAPPING.add(LDF_MAP_O, Hydra.PROPERTY, RDF.OBJECT, LDF_GRAPH);
	}
	
	private final static long PAGING = 50;
	private final static Value PAGING_VAL = F.createLiteral(PAGING);	

	private final static String Q_COUNT =
		"SELECT (COUNT(*) AS ?cnt) " + 
		"WHERE { ?s ?p ?o } ";
	
	private final static String Q_COUNT_GRAPH =
		"SELECT (COUNT(*) AS ?cnt) " + 
		"WHERE { GRAPH ?graph { ?s ?p ?o } } ";
	
	private final static String Q_LDF = 
		"CONSTRUCT { ?s ?p ?o } " +
		"WHERE { ?s ?p ?o } " +
		//"ORDER BY ?s ?p ?o " +
		"LIMIT " + PAGING;
	
	private final static String Q_LDF_GRAPH = 
		"CONSTRUCT { ?s ?p ?o } " +
		"WHERE { GRAPH ?graph { ?s ?p ?o } } " +
		//"ORDER BY ?s ?p ?o " +
		"LIMIT " + PAGING;
	
	
	/**
	 * Convert string into IRI or null
	 * @param s string
	 * @return IRI or null
	 */
	private static IRI createIRI(String s) {
		// Variable
		if (s.equals("") || (s.startsWith("?") && s.length() > 1)) {
			return null;
		}
		
		// IRI
		return F.createIRI(s);
	}
	
	/**
	 * Convert string into literal or URI
	 * 
	 * @param s object
	 * @return value (literal or URI)
	 */
	private static Value createLiteralOrUri(String s) {
		if (s.startsWith("\"")) {
			// test for simple literal
			if (s.endsWith("\"")) {
				return F.createLiteral(s.substring(1, s.length() - 2));
			}
			// test for language tag
			int l = s.lastIndexOf("\"@");
			if (l > 0) {
				return F.createLiteral(s.substring(1, l), s.substring(l + 2));
			}
			// test for data type
			int t = s.lastIndexOf("\"^^");
			if (l > 0) {
				return F.createLiteral(s.substring(1, l), s.substring(l + 3));
			}
			// malformed
			LOG.warn("Malformed object value");
			return null;
		}
		return createIRI(s);
	}
	
	/**
	 * Metadata of the fragment
	 * 
	 * @param fragment fragment IRI
	 * @param page page of the fragment
	 * @param count total number of results
	 * @return RDF triples
	 */
	private static Model meta(IRI page, IRI fragment, long count) {
		Model m = new LinkedHashModel();
		
		Value total = F.createLiteral(count);
		// as per spec two properties with same value
		m.add(page, Hydra.ITEMS, PAGING_VAL, LDF_GRAPH);
		m.add(page, VOID.TRIPLES, total, LDF_GRAPH);
		m.add(page, Hydra.TOTAL, total, LDF_GRAPH);
		m.add(page, Hydra.VIEW, fragment, LDF_GRAPH);
		//m.add(LDF_GRAPH, FOAF.PRIMARY_TOPIC, fragment);
		return m;
	}
	
	/**
	 * Hydra hypermedia controls
	 * 
	 * @param vocab vocabulary name
	 * @param dataset dataset IRI
	 * @param builder URI Builder
	 * @param offset offset
	 * @param count total number of triples
	 * @return RDF triples
	 */
	private static Model hyperControls(String vocab, IRI dataset, UriBuilder builder, 
										long offset, long count) {
		Model m = new LinkedHashModel();
	
		m.add(dataset, RDF.TYPE, VOID.DATASET, LDF_GRAPH);
		m.add(dataset, RDF.TYPE, Hydra.COLLECTION, LDF_GRAPH);
		
		IRI fragment = F.createIRI(builder.build().toString());
		m.add(dataset, VOID.SUBSET, fragment, LDF_GRAPH);

		// page count starts at 1
		long current = (offset % PAGING) + 1;
		builder.queryParam("page", "{page}");
		IRI page = F.createIRI(builder.build(current, "page").toString());
		
		// previous and next pages, if any
		if (offset >= PAGING) {
			URI prevPage = builder.build(current - 1, "page");
			m.add(dataset, Hydra.PREVIOUS, F.createIRI(prevPage.toString()), LDF_GRAPH);
		}
		if (offset + PAGING < count) {
			URI nextPage = builder.build(current + 1, "page");
			m.add(dataset, Hydra.NEXT, F.createIRI(nextPage.toString()), LDF_GRAPH);
		}
	
		// search template
		m.add(dataset, Hydra.SEARCH, LDF_SEARCH, LDF_GRAPH);
		m.add(LDF_SEARCH, Hydra.TEMPLATE, 
				F.createLiteral(PREFIX + "_ldf/" + vocab + "{?s,p,o}"), LDF_GRAPH);
		m.add(LDF_SEARCH, Hydra.MAPPING, LDF_MAP_S, LDF_GRAPH);
		m.add(LDF_SEARCH, Hydra.MAPPING, LDF_MAP_P, LDF_GRAPH);
		m.add(LDF_SEARCH, Hydra.MAPPING, LDF_MAP_O, LDF_GRAPH);
		
		// generic mapping
		m.addAll(HYDRA_MAPPING);
		
		m.addAll(meta(page, fragment, count));
		m.add(fragment, Hydra.ITEMS, PAGING_VAL);
		
		return m;
	}

	
	/**
	 * Get fragment / one page of results
	 * 
	 * @param conn repository
	 * @param subj subject IRI
	 * @param pred predicate IRI
	 * @param obj object value
	 * @param graph named graph
	 * @param offset
	 * @return RDF triples
	 */
	private static Model getFragment(RepositoryConnection conn, IRI subj, IRI pred, 
								Value obj, IRI graph, long offset, long count) {	
		// nothing (more) to show
		if ((count <= 0) || (offset >= count)) { 
			return new LinkedHashModel();
		}
		
		String qry = (graph != null) ? Q_LDF_GRAPH : Q_LDF;
		GraphQuery gq = conn.prepareGraphQuery(qry  + " OFFSET " + offset);
		
		if (subj != null) {
			gq.setBinding("s", subj);
		}
		if (pred != null) {
			gq.setBinding("p", pred);
		}
		if (obj != null) {
			gq.setBinding("o", obj);
		}
		if (graph != null) {
			gq.setBinding("graph", graph);
		}
		return QueryResults.asModel(gq.evaluate());
	}
	
	/**
	 * Count number of results
	 * 
	 * @param conn repository
	 * @param subj subject IRI
	 * @param pred predicate IRI
	 * @param obj object value
	 * @param graph named graph
	 * @return number of results
	 */
	private static long getCount(RepositoryConnection conn, 
									IRI subj, IRI pred, Value obj, IRI graph) {
		TupleQuery tq = conn.prepareTupleQuery((graph != null) ? Q_COUNT_GRAPH : Q_COUNT);
		if (subj != null) {
			tq.setBinding("s", subj);
		}
		if (pred != null) {
			tq.setBinding("p", pred);
		}
		if (obj != null) {
			tq.setBinding("o", obj);
		}
		if (graph != null) {
			tq.setBinding("graph", graph);
		}
		BindingSet res = QueryResults.singleResult(tq.evaluate());
		String val = res.getValue("cnt").stringValue();
		return Long.valueOf(val);
	}
	
	/**
	 * Set namespaces
	 * 
	 * @param m
	 * @return 
	 */
	private static Model setNamespaces(Model m) {
		Model ns = QueryHelper.setNamespaces(m);
		ns.setNamespace(Hydra.PREFIX, Hydra.NAMESPACE);
		ns.setNamespace(VOID.PREFIX, VOID.NAMESPACE);
		return ns;
	}
	
	/**
	 * Get linked data fragment
	 *
	 * @param repo RDF store 
	 * @param s subject to search for or null
	 * @param p predicate to search for or null
	 * @param o object to search for or null
	 * @param vocab named graph
	 * @param page page number
	 * @return RDF model 
	 */
	public static Model getLDF(Repository repo, String s, String p, String o, 
													String vocab, long page) {
		if (page < 1) {
			throw new WebApplicationException("Invalid (zero or negative) page number");
		}
		long offset = (page - 1) * PAGING;
		
		// speedup: vocabularies are stored in separate graphs
		IRI graph = (!vocab.isEmpty()) ? QueryHelper.asGraph(vocab) : null;
		
		IRI dataset = F.createIRI(PREFIX + "_ldf/" + vocab + "#dataset");
		
		UriBuilder builder  = UriBuilder.fromUri(PREFIX).path("_ldf").path(vocab);
		if (s != null) {
			builder = builder.queryParam("s", s);
		}
		if (p != null) {
			builder = builder.queryParam("p", p);
		}
		if (o != null) {
			builder = builder.queryParam("o", o);
		}

		IRI subj = (s != null) ? createIRI(s) : null;
		IRI pred = (p != null) ? createIRI(p) : null;
		Value obj = (o != null) ? createLiteralOrUri(o) : null;
		
		try (RepositoryConnection conn = repo.getConnection()) {
			long count = getCount(conn, subj, pred, obj, graph);
			
			Model m = new LinkedHashModel();
			m = setNamespaces(m);
			
			m.addAll(hyperControls(vocab, dataset, builder, offset, count));
			m.addAll(getFragment(conn, subj, pred, obj, graph, offset, count));
			
			return m;
		} catch (RepositoryException|MalformedQueryException|QueryEvaluationException e) {
			throw new WebApplicationException(e);
		}
	}
}
