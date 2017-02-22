/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.belgif.vocab.helpers;

import javax.ws.rs.WebApplicationException;

import org.eclipse.rdf4j.model.Model;

import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.MalformedQueryException;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.QueryResults;

import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;

/**
 * Helper class for querying the triple store
 * 
 * @author Bart.Hanssens
 */
public class QueryHelperFTS {
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
	 * Full text search
	 *
	 * @param repo RDF store 
	 * @param text text to search for
	 * @param from named graph
	 * @return RDF model 
	 */
	public static Model getFTS(Repository repo, String text, String from) {
		try (RepositoryConnection conn = repo.getConnection()) {
			GraphQuery gq = conn.prepareGraphQuery(QueryLanguage.SPARQL, Q_FTS);
			gq.setBinding("query", QueryHelper.asLiteral(text + "*"));
			gq.setBinding("graph", QueryHelper.asGraph(from));
	
			return QueryHelper.setNamespaces(QueryResults.asModel(gq.evaluate()));
		} catch (RepositoryException|MalformedQueryException|QueryEvaluationException e) {
			throw new WebApplicationException(e);
		}
	}
}
