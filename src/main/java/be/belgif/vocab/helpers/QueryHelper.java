/*
 * Copyright (c) 2017, Bart Hanssens <bart.hanssens@bosa.fgov.be>
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
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.DCAT;

import org.eclipse.rdf4j.model.vocabulary.DCTERMS;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.model.vocabulary.SHACL;
import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.eclipse.rdf4j.model.vocabulary.VCARD4;
import org.eclipse.rdf4j.model.vocabulary.VOID;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;

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

	// namespace mappings
	public final static Map<String, String> NS_MAP = new HashMap();

	static {
		NS_MAP.put(DCTERMS.PREFIX, DCTERMS.NAMESPACE);
		NS_MAP.put(FOAF.PREFIX, FOAF.NAMESPACE);
		NS_MAP.put(OWL.PREFIX, OWL.NAMESPACE);
		NS_MAP.put(RDF.PREFIX, RDF.NAMESPACE);
		NS_MAP.put(RDFS.PREFIX, RDFS.NAMESPACE);
		NS_MAP.put(SHACL.PREFIX, SHACL.NAMESPACE);
		NS_MAP.put(SKOS.PREFIX, SKOS.NAMESPACE);
		NS_MAP.put(VCARD4.PREFIX, VCARD4.NAMESPACE);
		NS_MAP.put(VOID.PREFIX, VOID.NAMESPACE);
		NS_MAP.put(XMLSchema.PREFIX, XMLSchema.NAMESPACE);
	}

	private final static String PREFIX = App.getPrefix();
	private final static String PREFIX_GRAPH = App.getPrefixGraph();

	private final static ValueFactory F = SimpleValueFactory.getInstance();

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
		return F.createIRI(PREFIX_GRAPH + name);
	}

	/**
	 * Get named graph / context id from name
	 *
	 * @param name
	 * @return context URI
	 */
	public static IRI asDataset(String name) {
		return F.createIRI(PREFIX + "void#" + name);
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
		if (!m.isEmpty()) {
			NS_MAP.forEach((p, n) -> m.setNamespace(p, n));
		}
		return m;
	}

	/**
	 * Get all triples from a graph
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
	 * Get list of 
	 * 
	 * @param repo RDF store
	 * @param obj type (RDF object) to retrieve
	 * @return list as triples
	 */
	public static Model getType(Repository repo, IRI obj) {
		Model m = new LinkedHashModel();

		try (RepositoryConnection conn = repo.getConnection();
			RepositoryResult<Statement> vocabs
				= conn.getStatements(null, RDF.TYPE, obj)) {
			while (vocabs.hasNext()) {
				Resource iri = vocabs.next().getSubject();
				Iterations.addAll(conn.getStatements(iri, null, null), m);
			}
		} catch (RepositoryException e) {
			throw new WebApplicationException(e);
		}
		
		return setNamespaces(m);
	
	}
	
	
	/**
	 * Get all contexts with ontologies
	 *
	 * @param repo RDF store
	 * @return list of ontologies
	 */
	public static Model getOntoList(Repository repo) {
		return getType(repo, OWL.ONTOLOGY);
	}
	
	/**
	 * Get all contexts with vocabularies
	 *
	 * @param repo RDF store
	 * @return list of vocabularies
	 */
	public static Model getVocabList(Repository repo) {
		return getType(repo, VOID.DATASET);
	}

	/**
	 * Get all contexts with xml namespaces
	 *
	 * @param repo RDF store
	 * @return list of xml namespaces
	 */
	public static Model getXmlnsList(Repository repo) {
		return getType(repo, DCAT.DISTRIBUTION);
	}
	
	/**
	 * Get all ontologies
	 *
	 * @param repo RDF store
	 * @return list of xml namespaces
	 */
	public static Model getOntologyList(Repository repo) {
		return getType(repo, OWL.ONTOLOGY);
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
