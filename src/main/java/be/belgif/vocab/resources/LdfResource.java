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

import be.belgif.vocab.helpers.RDFMediaType;
import be.belgif.vocab.ldf.QueryHelperLDF;

import com.codahale.metrics.annotation.ExceptionMetered;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.repository.Repository;

/**
 * Linked Data Fragments search.
 *
 * @author Bart.Hanssens
 */
@Path("/_ldf")
public class LdfResource extends RdfResource {

	@GET
	@Produces({RDFMediaType.TRIG, RDFMediaType.JSONLD})
	@ExceptionMetered
	public Model searchAll(@QueryParam("s") String s,
			@QueryParam("p") String p, @QueryParam("o") String o,
			@QueryParam("page") String page) {
		return QueryHelperLDF.getLDF(getRepository(), s, p, o, "", page);
	}

	@GET
	@Path("{vocab}")
	@Produces({RDFMediaType.TRIG, RDFMediaType.JSONLD})
	@ExceptionMetered
	public Model searchVocab(@PathParam("vocab") String vocab, @QueryParam("s") String s,
			@QueryParam("p") String p, @QueryParam("o") String o,
			@QueryParam("page") String page) {
		return QueryHelperLDF.getLDF(getRepository(), s, p, o, vocab, page);
	}

	/**
	 * Constructor
	 *
	 * @param repo RDF triple store
	 */
	public LdfResource(Repository repo) {
		super(repo);
	}
}
