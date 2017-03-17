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
package be.belgif.vocab.helpers;

import javax.ws.rs.WebApplicationException;

import org.eclipse.rdf4j.model.Model;

import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.MalformedQueryException;
import org.eclipse.rdf4j.query.QueryEvaluationException;
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

	private final static String Q_FTS
			= "PREFIX search: <http://www.openrdf.org/contrib/lucenesail#> " + "\n"
			+ "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> " + "\n"
			+ "CONSTRUCT { ?s skos:prefLabel ?o }"
			+ " WHERE { "
			+ " GRAPH ?graph { "
			+ " ?s search:matches [ search:query ?query ] ."
			+ " ?s skos:prefLabel ?o } . "
			+ "}";

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
			GraphQuery gq = conn.prepareGraphQuery(Q_FTS);
			gq.setBinding("query", QueryHelper.asLiteral(text + "*"));
			gq.setBinding("graph", QueryHelper.asGraph(from));

			return QueryHelper.setNamespaces(QueryResults.asModel(gq.evaluate()));
		} catch (RepositoryException | MalformedQueryException | QueryEvaluationException e) {
			throw new WebApplicationException(e);
		}
	}
}
