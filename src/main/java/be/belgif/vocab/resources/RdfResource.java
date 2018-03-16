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
package be.belgif.vocab.resources;

import be.belgif.vocab.helpers.QueryHelper;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.repository.Repository;

/**
 * Abstract resource querying the RDF triple store.
 *
 * @author Bart.Hanssens
 */
public abstract class RdfResource {

	private final Repository repo;

	/**
	 * Get repository
	 *
	 * @return repository
	 */
	protected Repository getRepository() {
		return repo;
	}

	/**
	 * Get triples by ID from a specific context
	 *
	 * @param url
	 * @param ctx
	 * @return RDF model
	 */
	protected Model getById(String url, IRI ctx) {
		return QueryHelper.getByID(repo, QueryHelper.asURI(url), ctx);
	}

	/**
	 * Get triples of a specific class (RDF type) across all contexts
	 *
	 * @param type IRI
	 * @return triples
	 */
	protected Model getByClass(IRI type) {
		return QueryHelper.getByClass(repo, type);
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
