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

import javax.ws.rs.WebApplicationException;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.VOID;
import org.eclipse.rdf4j.query.BindingSet;

import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.MalformedQueryException;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.query.TupleQuery;

import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for querying the triple store
 * 
 * @author Bart.Hanssens
 */
public class QueryHelperLDF {
	private final static Logger LOG = (Logger) LoggerFactory.getLogger(QueryHelperLDF.class);
	
	private final static ValueFactory F = SimpleValueFactory.getInstance();

	
	// Hydra mapping
	private final static Model HM = new LinkedHashModel();
	static {
		
		IRI s = F.createIRI(App.PREFIX + "ldf#s");
		HM.add(s, Hydra.VARIABLE, F.createLiteral("s"));
		HM.add(s, Hydra.PROPERTY, RDF.SUBJECT);
		
		IRI p = F.createIRI(App.PREFIX + "ldf#p");
		HM.add(p, Hydra.VARIABLE, F.createLiteral("p"));
		HM.add(p, Hydra.PROPERTY, RDF.PREDICATE);
		
		IRI o = F.createIRI(App.PREFIX + "ldf#o");
		HM.add(o, Hydra.VARIABLE, F.createLiteral("o"));
		HM.add(o, Hydra.PROPERTY, RDF.OBJECT);
		
		IRI mapping = F.createIRI(App.PREFIX + "ldf#mapping");
		HM.add(mapping, Hydra.MAPPING, s);
		HM.add(mapping, Hydra.MAPPING, p);
		HM.add(mapping, Hydra.MAPPING, o);
	}
	
	private final static long PAGE = 50;
	
	private final static String Q_COUNT =
		"SELECT COUNT(*) AS ?cnt " + 
		"WHERE { GRAPH ?graph { ?s ?p ?o } } ";
	
	private final static String Q_LDF = 
		"CONSTRUCT { ?s ?p?o } " +
		"WHERE { GRAPH ?graph { ?s ?p ?o } } " +
		"ORDER BY ?s " +
		"OFFSET ?off " +
		"LIMIT 50 ";
	
	
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
				return F.createLiteral(s);
			}
			// test for language tag
			int l = s.lastIndexOf("\"@");
			if (l > 0) {
				return F.createLiteral(s.substring(0, l), s.substring(l+1));
			}
			// test for data type
			// test for language tag
			int t = s.lastIndexOf("\"^^");
			if (l > 0) {
				return F.createLiteral(s.substring(0, l), s.substring(l+2));
			}
			// malformed
			LOG.warn("Malformed object value");
			return null;
		}
		return createIRI(s);
	}
	
	/**
	 * Hydra Hypermedia controls
	 * 
	 * @param dataset
	 * @param parent parent dataset
	 * @param prev previous page
	 * @param next next page
	 * @param count number of triples in total
	 * @return 
	 */
	private Model hyperControls(String vocab, IRI page, IRI parent, 
												IRI prev, IRI next, long count) {
		IRI search = F.createIRI(App.PREFIX + "ldf#search");
		
		Model m = new LinkedHashModel();
		m.setNamespace(Hydra.PREFIX, Hydra.NAMESPACE);
		m.setNamespace(VOID.PREFIX, VOID.NAMESPACE);
		
		m.add(page, VOID.SUBSET, parent);
		m.add(page, Hydra.SEARCH, search);
		m.add(search, Hydra.TEMPLATE, 
				F.createIRI(App.PREFIX + "ldf/" + vocab + "{?s, ?p, ?o}"));
		m.add(search, Hydra.MAPPING, F.createIRI(App.PREFIX + "ldf#mapping"));
		m.addAll(HM);
		return m;
	}

	
	/**
	 * Get paged results
	 * 
	 * @param conn repository
	 * @param subj subject IRI
	 * @param pred predicate IRI
	 * @param obj object value
	 * @param graph named graph
	 * @param offset
	 * @return 
	 */
	private static Model getPage(RepositoryConnection conn, 
						IRI subj, IRI pred, Value obj, IRI graph, long offset) {	
		GraphQuery gq = conn.prepareGraphQuery(Q_LDF);
		gq.setBinding("s", subj);
		gq.setBinding("p", pred);
		gq.setBinding("o", obj);
		gq.setBinding("graph", graph);
		gq.setBinding("offset", F.createLiteral(offset));
		
		return QueryHelper.setNamespaces(QueryResults.asModel(gq.evaluate()));
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
		TupleQuery tq = conn.prepareTupleQuery(Q_COUNT);
		tq.setBinding("s", subj);
		tq.setBinding("p", pred);
		tq.setBinding("o", obj);
		tq.setBinding("graph", graph);
		
		BindingSet res = QueryResults.singleResult(tq.evaluate());
		String val = res.getValue("cnt").stringValue();
		return Long.valueOf(val);
	}
	
	/**
	 * Get linked data fragment
	 *
	 * @param repo RDF store 
	 * @param s subject to search for or null
	 * @param p predicate to search for or null
	 * @param o object to search for or null
	 * @param vocab named graph
	 * @param offset offset number
	 * @return RDF model 
	 */
	public static Model getLDF(Repository repo, String s, String p, String o, 
													String vocab, long offset) {
		try (RepositoryConnection conn = repo.getConnection()) {
			IRI graph = QueryHelper.asGraph(vocab);
			IRI subj = (s != null) ? createIRI(s) : null;
			IRI pred = (p != null) ? createIRI(p) : null;
			Value obj = (o != null) ? createLiteralOrUri(o) : null;
			
			long count = getCount(conn, subj, pred, obj, graph);
			
			Model data = ((count > 0) && (offset < count)) 
								? getPage(conn, subj, pred, obj, graph, offset) 
								: new LinkedHashModel();
			
			Model controls = hyperControls();
			
		} catch (RepositoryException|MalformedQueryException|QueryEvaluationException e) {
			throw new WebApplicationException(e);
		}
	}
}
