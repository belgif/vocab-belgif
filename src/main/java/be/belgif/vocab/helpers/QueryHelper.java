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
import be.belgif.vocab.ldf.Hydra;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

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
import org.eclipse.rdf4j.model.vocabulary.ORG;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.model.vocabulary.ROV;
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
	public final static String DATASET = "dataset";
	public final static String ONTO = "onto";
	public final static String SHACLS = "shacl";
	public final static String VOCAB = "vocab";
	public final static String XMLNS = "xmlns";
	
	// namespace mappings
	public final static BiMap<String,String> NS_MAP = HashBiMap.create();

	static {
		NS_MAP.put(DCAT.PREFIX, DCAT.NAMESPACE);
		NS_MAP.put(DCTERMS.PREFIX, DCTERMS.NAMESPACE);
		NS_MAP.put(FOAF.PREFIX, FOAF.NAMESPACE);
		NS_MAP.put(ORG.PREFIX, ORG.NAMESPACE);
		NS_MAP.put(OWL.PREFIX, OWL.NAMESPACE);
		NS_MAP.put(RDF.PREFIX, RDF.NAMESPACE);
		NS_MAP.put(RDFS.PREFIX, RDFS.NAMESPACE);
		NS_MAP.put(ROV.PREFIX, ROV.NAMESPACE);
		NS_MAP.put(SHACL.PREFIX, SHACL.NAMESPACE);
		NS_MAP.put(SKOS.PREFIX, SKOS.NAMESPACE);
		NS_MAP.put(VCARD4.PREFIX, VCARD4.NAMESPACE);
		NS_MAP.put(VOID.PREFIX, VOID.NAMESPACE);
		NS_MAP.put(XMLSchema.PREFIX, XMLSchema.NAMESPACE);
		NS_MAP.put(Hydra.PREFIX, Hydra.NAMESPACE);
		
		// Joinup Core Vocabularies
		NS_MAP.put("cv", "http://data.europa.eu/m8g/");
		NS_MAP.put("schema", "http://schema.org/");
	}

	private final static String PREFIX = App.getPrefix();

	private final static ValueFactory F = SimpleValueFactory.getInstance();

	/**
	 * Get string as URI
	 *
	 * @param uri
	 * @return URI representation
	 */
	public static IRI asURI(String uri) {
		return (uri != null) ?  F.createIRI(uri) : null;
	}

	/**
	 * Get named graph / RDF4J context
	 * 
	 * @param type
	 * @param name
	 * @return context IRI 
	 */
	public static IRI getGraphName(String type, String name) {
		return F.createIRI(PREFIX + "graph/" + type + "/" + name);
	}

	/**
	 * Get named graph / context id from name
	 *
	 * @param name
	 * @return context URI
	 */
	public static IRI getDatasetName(String name) {
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
	 * Get all triples for a given subject from a named graph
	 *
	 * @param repo RDF store
	 * @param subj subject IRI or null
	 * @param ctx named graph
	 * @return list of triples
	 */
	public static Model getByID(Repository repo, IRI subj, IRI ctx) {
		Model m = new LinkedHashModel();

		try (RepositoryConnection conn = repo.getConnection()) {
			Iterations.addAll(
				conn.getStatements(subj, null, null, ctx), m);
		} catch (RepositoryException e) {
			throw new WebApplicationException(e);
		}
		//return setNamespaces(m);
		return m;
	}

	
	/**
	 * Get list of all instances of a specific class
	 * 
	 * @param repo RDF store
	 * @param type type (RDF class) to retrieve
	 * @return list as triples
	 */
	public static Model getByClass(Repository repo, IRI type) {
		Model m = new LinkedHashModel();

		try (RepositoryConnection conn = repo.getConnection();
			RepositoryResult<Statement> res
				= conn.getStatements(null, RDF.TYPE, type)) {
			while (res.hasNext()) {
				Resource iri = res.next().getSubject();
				Iterations.addAll(conn.getStatements(iri, null, null), m);
			}
		} catch (RepositoryException e) {
			throw new WebApplicationException(e);
		}
		
		//return setNamespaces(m);
		return m;
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
