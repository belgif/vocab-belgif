/*
 * Copyright (c) 2018, Bart Hanssens <bart.hanssens@bosa.fgov.be>
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
import be.belgif.vocab.helpers.RDFMediaType;
import be.belgif.vocab.views.OntoListView;
import be.belgif.vocab.views.OwlView;
import be.belgif.vocab.views.ShaclListView;
import be.belgif.vocab.views.ShaclView;

import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.repository.Repository;

/**
 * SHACL shapes
 *
 * @author Bart Hanssens
 */
@Path("/shacl")
public class ShaclResource extends RdfResource {
	private final String shaclDir;
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public ShaclListView getListHTML(@QueryParam("lang") Optional<String> lang) {
		return new ShaclListView(getByClass(OWL.ONTOLOGY), lang.orElse("en"));
	}
	
	@GET
	@Path("/{shacl}")
	@Produces(MediaType.TEXT_HTML)
	public ShaclView getShaclHTML(@PathParam("shacl") String shacl,
				@QueryParam("lang") Optional<String> lang) {
		IRI ctx = QueryHelper.getGraphName(QueryHelper.SHACLS, shacl);
		Model m = getById(null, ctx);
		return new ShaclView(shacl, m, lang.orElse("en"));
	}
	
	@GET
	@Path("{file: .+\\.(jsonld|nt|ttl)}")
	@Produces({RDFMediaType.JSONLD, RDFMediaType.NTRIPLES, RDFMediaType.TURTLE})
	public File getShaclFile(@PathParam("file") String file) {
		return Paths.get(this.shaclDir, file).toFile();
	}
	
	@GET
	@Path("/{shacl}")
	@Produces({RDFMediaType.JSONLD, RDFMediaType.NTRIPLES, RDFMediaType.TURTLE})
	public File getShaclAsFile(@PathParam("shacl") String shacl) {
		return Paths.get(this.shaclDir, shacl).toFile();
	}
	
	/**
	 * Constructor
	 *
	 * @param repo RDF triple store
	 * @param shaclDir
	 */
	public ShaclResource(Repository repo, String shaclDir) {
		super(repo);
		this.shaclDir = shaclDir;
	}
}
