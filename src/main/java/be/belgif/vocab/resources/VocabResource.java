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

import be.belgif.vocab.App;
import be.belgif.vocab.helpers.QueryHelper;
import be.belgif.vocab.helpers.RDFMediaType;
import be.belgif.vocab.views.VocabTermView;
import be.belgif.vocab.views.VocabView;

import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.repository.Repository;

/**
 * Vocabulary term of a SKOS thesaurus.
 *
 * @author Bart.Hanssens
 */
@Path("/auth")
public class VocabResource extends RdfResource {
	private final static String PREFIX = App.getPrefix() + "auth/";

	/**
	 * Get URI for the term.
	 * Remember, HTTP request does NOT contain '#' or anything after that
	 * 
	 * @param vocab vocabulary name
	 * @param term term name
	 * @return 
	 */
	private Model get(String vocab, String term) {
		String url = (!term.isEmpty()) 
				? PREFIX + vocab + "/" + term + "#id"
				: PREFIX + vocab + "#id";
		IRI ctx = QueryHelper.getGraphName(QueryHelper.VOCAB, vocab);
		return getById(url, ctx);
	}
	
	@GET
	@Path("/{vocab}")
	@Produces({RDFMediaType.JSONLD, RDFMediaType.NTRIPLES, RDFMediaType.TURTLE})
	public Model getVocabRDF(@PathParam("vocab") String vocab) {
		return get(vocab, "");
	}

	@GET
	@Path("/{vocab}")
	@Produces(MediaType.TEXT_HTML)
	public VocabView getVocabHTML(@PathParam("vocab") String vocab,
			@QueryParam("lang") Optional<String> lang) {
		return new VocabView(vocab, get(vocab, "") , lang.orElse("en"));
	}

	@GET
	@Path("/{vocab}/{term}")
	@Produces({RDFMediaType.JSONLD, RDFMediaType.NTRIPLES, RDFMediaType.TURTLE})
	public Model getVocabTermRDF(@PathParam("vocab") String vocab,
			@PathParam("term") String term) {
		return get(vocab, term);
	}

	@GET
	@Path("/{vocab}/{term}")
	@Produces(MediaType.TEXT_HTML)
	public VocabTermView getVocabTermHTML(@PathParam("vocab") String vocab,
			@PathParam("term") String term,
			@QueryParam("lang") Optional<String> lang) {
		return new VocabTermView(vocab, get(vocab, term), lang.orElse("en"));
	}

	/**
	 * Constructor
	 *
	 * @param repo RDF triple store
	 */
	public VocabResource(Repository repo) {
		super(repo);
	}
}
