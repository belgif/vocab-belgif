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
import be.belgif.vocab.views.OntoView;

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
import org.eclipse.rdf4j.model.vocabulary.DCAT;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.repository.Repository;

/**
 * SHACL validation rules
 *
 * @author Bart Hanssens
 */
@Path("/shacl")
public class ShaclResource extends RdfResource {
	private final String shaclDir;
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public OntoListView getShaclHTML(@QueryParam("lang") Optional<String> lang) {
		return new OntoListView(getByClass(DCAT.DISTRIBUTION), 
					getByClass(OWL.ONTOLOGY), lang.orElse("en"));
	}
	
		
	@GET
	@Path("{file: .+\\.jsonld}")
	@Produces({RDFMediaType.JSONLD})
	public File getJsonFile(@PathParam("file") String file) {
		return Paths.get(this.shaclDir, file).toFile();
	}
	@GET
	@Path("/{onto}")
	@Produces({RDFMediaType.JSONLD})
	public File getJsonDefFile(@PathParam("onto") String onto) {
		return getJsonFile(onto + ".jsonld");
	}

	@GET
	@Path("{file: .+\\.nt}")
	@Produces({RDFMediaType.NTRIPLES})
	public File getNtFile(@PathParam("file") String file) {
		return Paths.get(this.shaclDir, file).toFile();
	}
	@GET
	@Path("/{onto}")
	@Produces({RDFMediaType.NTRIPLES})
	public File getNtDefFile(@PathParam("onto") String onto) {
		return getNtFile(onto + ".nt");
	}
	
	@GET
	@Path("{file: .+\\.ttl}")
	@Produces({RDFMediaType.TTL})
	public File getTtlFile(@PathParam("file") String file) {
		return Paths.get(this.shaclDir,
			file.endsWith(".ttl") ? file : file + ".ttl").toFile();
	}
	@GET
	@Path("/{onto}")
	@Produces({RDFMediaType.TTL})
	public File getTtlDefFile(@PathParam("onto") String onto) {
		return getNtFile(onto + ".ttl");
	}	
	

	@GET
	@Path("/{onto}")
	@Produces(MediaType.TEXT_HTML)
	public OntoView getOntoHTML(@PathParam("onto") String onto,
				@QueryParam("lang") Optional<String> lang) {
		//String subj = PREFIX + onto + "#";
		IRI ctx = QueryHelper.getGraphName(QueryHelper.ONTO, onto);
		Model m = getById(null, ctx);
		return new OntoView(onto, m, lang.orElse("en"));
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
