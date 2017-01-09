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
package be.belgif.vocab.resources;

import be.belgif.vocab.App;
import be.belgif.vocab.helpers.RDFMediaType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

import org.eclipse.rdf4j.common.iteration.Iterations;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.DCTERMS;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.MalformedQueryException;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.QueryResults;

import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.RepositoryResult;


/**
 * Abstract resource querying the RDF triple store.
 * 
 * @author Bart.Hanssens
 */

@Produces({RDFMediaType.JSONLD, RDFMediaType.NTRIPLES, RDFMediaType.TTL})
public abstract class RdfResource {
	private final ValueFactory fac = SimpleValueFactory.getInstance();
	private final Repository repo;
	
	private final static String Q_IRI = 
			"CONSTRUCT { ?s ?p ?o }"
			+ " WHERE { ?s ?p ?o }";
	
	/**
	 * Get string as URI
	 * 
	 * @param uri
	 * @return URI representation
	 */
	protected IRI asURI(String uri) {
		return fac.createIRI(uri);
	}
	
	/**
	 * Get string as RDF literal
	 * 
	 * @param lit
	 * @return literal 
	 */
	protected Literal asLiteral(String lit) {
		return fac.createLiteral(lit);
	}
	
	/**
	 * Prepare and run a SPARQL update
	 * 
	 * @param upd update string
	 */
	/*protected void update(String upd) {
		try (RepositoryConnection conn = this.repo.getConnection()) {
			Update uq = conn.prepareUpdate(QueryLanguage.SPARQL, upd);
			uq.execute();
		} catch (RepositoryException|MalformedQueryException|QueryEvaluationException e) {
			throw new WebApplicationException(e);
		}
	}*/
	
	/**
	 * Prepare and run a SPARQL query
	 *
	 * @param qry query string
	 * @param bindings bindings (if any)
	 * @return results in triple model
	 */
	protected Model query(String qry, Map<String,Value> bindings) {
		try (RepositoryConnection conn = this.repo.getConnection()) {
			GraphQuery gq = conn.prepareGraphQuery(QueryLanguage.SPARQL, qry);
			bindings.forEach((k,v) -> gq.setBinding(k, v));
			
			Model m = QueryResults.asModel(gq.evaluate());
			if (! m.isEmpty()) {
				m.setNamespace(DCTERMS.PREFIX, DCTERMS.NAMESPACE);
				m.setNamespace(FOAF.PREFIX, FOAF.NAMESPACE);
				m.setNamespace(OWL.PREFIX, OWL.NAMESPACE);
				m.setNamespace(RDF.PREFIX, RDF.NAMESPACE);
				m.setNamespace(SKOS.PREFIX, SKOS.NAMESPACE);
			}
			return m;
		} catch (RepositoryException|MalformedQueryException|QueryEvaluationException e) {
			throw new WebApplicationException(e);
		}
	}
	
	/**
	 * Get by ID (URI)
	 * 
	 * @param prefix
	 * @param type
	 * @param id
	 * @return RDF model 
	 */
	protected Model getById(String prefix, String type, String id) {
		String url = (!id.isEmpty()) ? prefix + type + "/" + id + "#id"
									: prefix + type + "#id";
		return getById(url);
	}
	
	/**
	 * Get all contexts a.k.a. vocabularies
	 * 
	 * @return list of graphs
	 */
	protected Model getAllVocabs() {
		Model m = new LinkedHashModel();
		
		try (RepositoryConnection conn = this.repo.getConnection()) {
			RepositoryResult<Statement> vocabs = 
					conn.getStatements(null, RDF.TYPE, SKOS.CONCEPT_SCHEME);
			while(vocabs.hasNext()) {
				Resource iri = vocabs.next().getSubject();
				Iterations.addAll(conn.getStatements(iri, DCTERMS.TITLE, null), m);
				Iterations.addAll(conn.getStatements(iri, DCTERMS.DESCRIPTION, null), m);
			}
		} catch (RepositoryException e) {
			throw new WebApplicationException(e);
		}
		return m;
	}
	
	/**
	 * Get all triples
	 * 
	 * @param from named graph
	 * @return all triples in a graph
	 */
	protected Model getAll(String from) {
		Model m = new LinkedHashModel();
		
		IRI vocab = fac.createIRI(App.PREFIX + from + "#id");
		IRI ctx = fac.createIRI(App.PREFIX_GRAPH + from);
		
		try (RepositoryConnection conn = this.repo.getConnection()) {
			Iterations.addAll(conn.getStatements(vocab, null, null, ctx), m);
		} catch (RepositoryException e) {
			throw new WebApplicationException(e);
		}
		return m;
	}
	
	/**
	 * Get by ID (URI)
	 * 
	 * @param url
	 * @return RDF model 
	 */
	protected Model getById(String url) {
		Map<String,Value> map = new HashMap();
		map.put("s", asURI(url));
		return query(Q_IRI, map);		
	}
	
	/**
	 * Incremental update for Lucene FTS
	 */
/*	protected void incrementFTS() {
		update(INCR_INDEX);
	}
*/	
	/**
	 * Full text search
	 * 
	 * @param text text to search for
	 * @return RDF model 
	 */
/*	protected Model getFTS(String text) {
		return getFTS(text, null);
	}
*/
	/**
	 * Full text search
	 * 
	 * @param text text to search for
	 * @param from named graph
	 * @return RDF model 
	 */
	/*protected Model getFTS(String text, String from) {
		String qry = Q_FTS;
		Map<String,Value> map = new HashMap();
		map.put("fts", asLiteral(text + "*"));
		if (from != null) {
			qry = qry.replaceFirst("WHERE", "FROM <" + from + "> WHERE");
		}
		return query(qry, map);
	}*/
	
	/**
	 * Put statements in the store
	 * 
	 * @param m 
	 */
	protected void putStatements(Model m) {
		try (RepositoryConnection conn = this.repo.getConnection()) {
			conn.add(m);
		} catch (RepositoryException e) {
			throw new WebApplicationException(e);
		}
	}
	
	/**
	 * Delete all triples for subject URL
	 * 
	 * @param url subject to delete
	 */
	protected void deleteStatements(String url) {
		try (RepositoryConnection conn = this.repo.getConnection()) {
			conn.remove(fac.createIRI(url), null, null);
		} catch (RepositoryException e) {
			throw new WebApplicationException(e);
		}
	}
	
	/**
	 * Constructor
	 * 
	 * @param repo 
	 */
	public RdfResource(Repository repo) {
		this.repo = repo;
	}
}

