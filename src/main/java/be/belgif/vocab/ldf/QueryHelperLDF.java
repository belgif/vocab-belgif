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
import org.eclipse.rdf4j.model.BNode;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.VOID;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
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
	
	public final static String LDF = "_ldf";
	
	private final static ValueFactory F = SimpleValueFactory.getInstance();
	
	private final static String PREFIX = App.getPrefix();
	private final static BNode LDF_SEARCH = F.createBNode("search");
	
	private final static Value S = F.createLiteral("s");
	private final static Value P = F.createLiteral("p");
	private final static Value O = F.createLiteral("o");
	
	private final static BNode LDF_MAP_S = F.createBNode("s");
	private final static BNode LDF_MAP_P = F.createBNode("p");
	private final static BNode LDF_MAP_O = F.createBNode("o");
	
	private final static String PAGE = "page";
	
	private final static int PAGING = 50;
	private final static Value PAGING_VAL = F.createLiteral("50", XMLSchema.INTEGER);
	

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
	 * Hydra counters and other metadata
	 * 
	 * @param m triples
	 * @param graph hydra named graph
	 * @param part page or fragment IRI
	 * @param count total number of results
	 * @return RDF triples
	 */
	private static void meta(Model m, IRI graph, IRI part, int count) {
		m.add(graph, FOAF.PRIMARY_TOPIC, part, graph);
		
		Value total = F.createLiteral(String.valueOf(count), XMLSchema.INTEGER);
		m.add(part, RDF.TYPE, Hydra.PARTIAL, graph);
		m.add(part, Hydra.ITEMS, PAGING_VAL, graph);
		// as per spec two properties with same value
		m.add(part, VOID.TRIPLES, total, graph);
		m.add(part, Hydra.TOTAL, total, graph);
	}
	
	/**
	 * Hydra search template mappings
	 * 
	 * @param m triples
	 * @param graph hydra graph
	 * @param vocab name of the vocabulary
	 * @param dataset dataset IRI
	 */
	private static void template(Model m, IRI graph, String vocab, IRI dataset) {
		// search template
		m.add(dataset, Hydra.SEARCH, LDF_SEARCH, graph);
		m.add(LDF_SEARCH, Hydra.TEMPLATE, 
				F.createLiteral(PREFIX + LDF + "/" + vocab + "{?s,p,o}"), graph);
		m.add(LDF_SEARCH, Hydra.MAPPING, LDF_MAP_S, graph);
		m.add(LDF_SEARCH, Hydra.MAPPING, LDF_MAP_P, graph);
		m.add(LDF_SEARCH, Hydra.MAPPING, LDF_MAP_O, graph);
		
		// generic mapping
		m.add(LDF_MAP_S, Hydra.VARIABLE, S, graph);
		m.add(LDF_MAP_S, Hydra.PROPERTY, RDF.SUBJECT, graph);
		m.add(LDF_MAP_P, Hydra.VARIABLE, P, graph);
		m.add(LDF_MAP_P, Hydra.PROPERTY, RDF.PREDICATE, graph);
		m.add(LDF_MAP_O, Hydra.VARIABLE, O, graph);
		m.add(LDF_MAP_O, Hydra.PROPERTY, RDF.OBJECT, graph);
	}
		
	/**
	 * Hydra pagination
	 * 
	 * @param m triples
	 * @param graph hydra named graph
	 * @param part page or fragment
	 * @param builder URI Builder
	 * @param current current page number
	 * @param count total number of results
	 * @param offset offset
	 */
	private static void page(Model m, IRI graph, IRI part, int current, int count, 
											int offset, UriBuilder builder) {
		// pagination, page count starts at 1		
		
		if (offset >= PAGING) {
			URI prevPage = builder.build(current - 1, PAGE);
			m.add(part, Hydra.PREVIOUS, F.createIRI(prevPage.toString()), graph);
		}
		if (offset + PAGING < count) {
			URI nextPage = builder.build(current + 1, PAGE);
			m.add(part, Hydra.NEXT, F.createIRI(nextPage.toString()), graph);
		}
		
	}
	
	/**
	 * Hydra hypermedia controls
	 * 
	 * @param m triples
	 * @param vocab vocabulary name
	 * @param dataset dataset IRI
	 * @param builder URI Builder
	 * @param offset offset
	 * @param count total number of triples
	 * @param isFrag true if fragment was requested (true if page)
	 */
	private static void hyperControls(Model m, String vocab, IRI dataset,
						UriBuilder builder, int offset, int count, boolean isFrag) {
		IRI graph = QueryHelper.asGraph("/" + vocab + "#hydra");
		IRI fragment = F.createIRI(builder.build().toString());
	
		builder.queryParam(PAGE, "{page}");
		int current = (offset / PAGING) + 1;
		IRI page = F.createIRI(builder.build(current, PAGE).toString());
			
		m.add(dataset, RDF.TYPE, VOID.DATASET, graph);
		m.add(dataset, RDF.TYPE, Hydra.COLLECTION, graph);
		m.add(dataset, VOID.SUBSET, fragment, graph);
		m.add(fragment, VOID.SUBSET, page, graph);

		template(m, graph, vocab, dataset);
		meta(m, graph, isFrag ? fragment : page, count);
		page(m, graph, isFrag ? fragment : page, current, count, offset, builder); 
	}

	
	/**
	 * Get fragment / one page of results
	 * 
	 * @param m triples
	 * @param conn repository
	 * @param subj subject IRI
	 * @param pred predicate IRI
	 * @param obj object value
	 * @param graph named graph
	 * @param offset
	 */
	private static void getFragment(Model m, RepositoryConnection conn, IRI subj, IRI pred, 
								Value obj, IRI graph, long offset, long count) {	
		// nothing (more) to show
		if ((count <= 0) || (offset >= count)) { 
			return;
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
		m.addAll(QueryResults.asModel(gq.evaluate()));
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
	private static int getCount(RepositoryConnection conn, 
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
		return Integer.valueOf(val);
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
													String vocab, String page) {
		boolean isFrag = (page == null || page.isEmpty());

		// check parameters
		int pageVal = isFrag ? 1 : Integer.valueOf(page);
		if (pageVal < 1) {
			throw new WebApplicationException("Invalid (zero or negative) page number");
		}
		
		IRI subj = (s != null) ? createIRI(s) : null;
		IRI pred = (p != null) ? createIRI(p) : null;
		Value obj = (o != null) ? createLiteralOrUri(o) : null;
	
		
		UriBuilder builder  = UriBuilder.fromUri(PREFIX).path(LDF).path(vocab);
		if (s != null) {
			builder = builder.queryParam("s", s);
		}
		if (p != null) {
			builder = builder.queryParam("p", p);
		}
		if (o != null) {
			builder = builder.queryParam("o", o);
		}

		int offset = (pageVal - 1) * PAGING;
		
		// speedup: vocabularies are stored in separate graphs
		IRI graph = (!vocab.isEmpty()) ? QueryHelper.asGraph(vocab) : null;
		IRI dataset = QueryHelper.asDataset(vocab);
		
		try (RepositoryConnection conn = repo.getConnection()) {
			int count = getCount(conn, subj, pred, obj, graph);
			
			Model m = new LinkedHashModel();
			
			hyperControls(m, vocab, dataset, builder, offset, count, isFrag);
			getFragment(m, conn, subj, pred, obj, graph, offset, count);

			return setNamespaces(m);
		} catch (RepositoryException|MalformedQueryException|QueryEvaluationException e) {
			throw new WebApplicationException(e);
		}
	}
}
