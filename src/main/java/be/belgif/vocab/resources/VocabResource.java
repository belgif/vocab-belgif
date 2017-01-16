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
import be.belgif.vocab.views.VocabTermView;

import com.codahale.metrics.annotation.ExceptionMetered;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.repository.Repository;

/**
 * Vocabulary term of a SKOS thesaurus.
 * 
 * @author Bart.Hanssens
 */
@Path("/{type}")
public class VocabResource extends RdfResource {

	@GET
	@Produces({RDFMediaType.JSONLD, RDFMediaType.NTRIPLES, RDFMediaType.TTL})
	@ExceptionMetered
	public Model getVocabRDF(@PathParam("type") String type) {
		System.err.println("get type");
		return getById(App.PREFIX, type, "");
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@ExceptionMetered
	public VocabTermView getVocabListHTML(@PathParam("type") String type, 
											@QueryParam("lang") Optional<String> lang) {
		System.err.println("get type html");
		return new VocabTermView(getById(App.PREFIX, type, ""), lang.orElse("en"));
	}
	
	@GET
	@Path("/{id}")
	@Produces({RDFMediaType.JSONLD, RDFMediaType.NTRIPLES, RDFMediaType.TTL})
	@ExceptionMetered
	public Model getVocabTermRDF(@PathParam("type") String type, 
												@PathParam("id") String id) {
				System.err.println("get type / id RDF" );

		return getById(App.PREFIX, type, id);
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.TEXT_HTML)
	@ExceptionMetered
	public VocabTermView getVocabTermHTML(@PathParam("type") String type, 
											@PathParam("id") String id, 
											@QueryParam("lang") Optional<String> lang) {
		System.err.println("get type / id");
		return new VocabTermView(getById(App.PREFIX, type, id), lang.orElse("en"));
	}
	
		
	@GET
	@Path("/_search")
	@Produces({RDFMediaType.JSONLD, RDFMediaType.NTRIPLES, RDFMediaType.TTL})
	@ExceptionMetered
	public Model search(@PathParam("type") String type, @QueryParam("q") String text) {
		return getFTS(text, type);
	}
	
	@GET
	@Path("/_download")
	@Produces({RDFMediaType.JSONLD, RDFMediaType.NTRIPLES, RDFMediaType.TTL}) 
	@ExceptionMetered
	public Model getGraphs(@PathParam("type") String type) {
		return get(null, type);
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
