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

import be.belgif.vocab.helpers.RDFMediaType;
import java.io.File;
import java.nio.file.Paths;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Full downloads of vocabularies
 *
 * @author Bart Hanssens
 */
@Path("/dataset")
public class DatasetResource {
	private final String vocabDir;
	
	@GET
	@Path("{file: .+\\.nt}")
	@Produces(RDFMediaType.NTRIPLES+";charset=utf-8")
	public File getFileNT(@PathParam("file") String file) {
		return Paths.get(vocabDir, file).toFile();
	}
	@GET
	@Path("{file: .+\\.jsonld}")
	@Produces(RDFMediaType.JSONLD+";charset=utf-8")
	public File getFileJSON(@PathParam("file") String file) {
		return Paths.get(vocabDir, file).toFile();
	}
	@GET
	@Path("{file: .+\\.ttl}")
	@Produces(RDFMediaType.TURTLE+";charset=utf-8")
	public File getFileTTL(@PathParam("file") String file) {
		return Paths.get(vocabDir, file).toFile();
	}
	/**
	 * Constructor
	 *
	 * @param vocabDir
	 */
	public DatasetResource(String vocabDir) {
		this.vocabDir = vocabDir;
	}
}
