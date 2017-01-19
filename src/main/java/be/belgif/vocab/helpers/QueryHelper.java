/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.belgif.vocab.helpers;

import be.belgif.vocab.App;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.WebApplicationException;
import org.eclipse.rdf4j.common.iteration.Iterations;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.DCTERMS;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.eclipse.rdf4j.model.vocabulary.VOID;

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
 * Helper class for querying the triple store
 * 
 * @author Bart.Hanssens
 */
public class QueryHelper {
	private final static ValueFactory F = SimpleValueFactory.getInstance();
	
	private final static String Q_FTS = 
		"PREFIX search: <http://www.openrdf.org/contrib/lucenesail#> " + "\n" +
		"PREFIX skos: <http://www.w3.org/2004/02/skos/core#> " + "\n" +
		"CONSTRUCT { ?s skos:prefLabel ?o }" +
		" WHERE { " +
			" GRAPH ?graph { " +
				" ?s search:matches [ search:query ?query ] ." +
				" ?s skos:prefLabel ?o } . " +
		"}";

	/**
	 * Get string as URI
	 * 
	 * @param uri
	 * @return URI representation
	 */
	public static IRI asURI(String uri) {
		return F.createIRI(uri);
	}
	
	/**
	 * Get name graph + context id from name
	 * 
	 * @param name
	 * @return context URI 
	 */
	public static IRI asGraph(String name) {
		return F.createIRI(App.PREFIX_GRAPH + name);
	}
	
	/**
	 * Get string as RDF literal
	 * 
	 * @param lit
	 * @return literal 
	 */
	public static Literal asLiteral(String lit) {
		return F.createLiteral(lit);
	}

	/**
	 * Add namespaces to triple model
	 * 
	 * @param m model
	 * @return model with namespaces
	 */
	public static Model setNamespaces(Model m) {
		if (! m.isEmpty()) {
			m.setNamespace(DCTERMS.PREFIX, DCTERMS.NAMESPACE);
			m.setNamespace(FOAF.PREFIX, FOAF.NAMESPACE);
			m.setNamespace(OWL.PREFIX, OWL.NAMESPACE);
			m.setNamespace(RDF.PREFIX, RDF.NAMESPACE);
			m.setNamespace(RDFS.PREFIX, RDFS.NAMESPACE);
			m.setNamespace(SKOS.PREFIX, SKOS.NAMESPACE);
			m.setNamespace(VOID.PREFIX, VOID.NAMESPACE);
		}
		return m;
	}
	
	/**
	 * Get all triples
	 * 
	 * @param repo RDF store
	 * @param subj subject IRI or null
	 * @param from named graph
	 * @return all triples in a graph
	 */
	public static Model get(Repository repo, IRI subj, String from) {
		Model m = new LinkedHashModel();
	
		try (RepositoryConnection conn = repo.getConnection()) {
			Iterations.addAll(conn.getStatements(subj, null, null, asGraph(from)), m);
		} catch (RepositoryException e) {
			throw new WebApplicationException(e);
		}
		return setNamespaces(m);
	}
	
	/**
	 * Prepare and run a SPARQL query
	 *
	 * @param repo repository
	 * @param qry query string
	 * @param bindings bindings (if any)
	 * @return results in triple model
	 */
	public static Model query(Repository repo, String qry, Map<String,Value> bindings) {
		try (RepositoryConnection conn = repo.getConnection()) {
			GraphQuery gq = conn.prepareGraphQuery(QueryLanguage.SPARQL, qry);
			bindings.forEach((k,v) -> gq.setBinding(k, v));

			return setNamespaces(QueryResults.asModel(gq.evaluate()));
		} catch (RepositoryException|MalformedQueryException|QueryEvaluationException e) {
			throw new WebApplicationException(e);
		}
	}

	/**
	 * Full text search
	 *
	 * @param repo RDF store 
	 * @param text text to search for
	 * @param from named graph
	 * @return RDF model 
	 */
	public static Model getFTS(Repository repo, String text, String from) {
		String qry = Q_FTS;
		Map<String,Value> map = new HashMap();
		map.put("query", asLiteral(text + "*"));
		map.put("graph", asGraph(from));
		return QueryHelper.query(repo, qry, map);
	}
	
	/**
	 * Get all contexts a.k.a. vocabularies
	 *
	 * @param repo RDF store 
	 * @return list of vocabularies
	 */
	public static Model getVocabList(Repository repo) {
		Model m = new LinkedHashModel();

		try (RepositoryConnection conn = repo.getConnection()) {
			RepositoryResult<Statement> vocabs = 
					conn.getStatements(null, RDF.TYPE, VOID.DATASET);
			while(vocabs.hasNext()) {
				Resource iri = vocabs.next().getSubject();
				Iterations.addAll(conn.getStatements(iri, null, null), m);
			}
		} catch (RepositoryException e) {
			throw new WebApplicationException(e);
		}
		return setNamespaces(m);
	}
	
	/**
	 * Put statements in the store
	 *
	 * @param repo RDF store
	 * @param m triples
	 */
	public static void putStatements(Repository repo, Model m) {
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.add(m);
		} catch (RepositoryException e) {
			throw new WebApplicationException(e);
		}
	}
	
	/**
	 * Delete all triples for subject URL
	 * 
	 * @param repo RDF store
	 * @param url subject to delete
	 */
	public static void deleteStatements(Repository repo, String url) {
		try (RepositoryConnection conn = repo.getConnection()) {
			conn.remove(F.createIRI(url), null, null);
		} catch (RepositoryException e) {
			throw new WebApplicationException(e);
		}
	}
}
