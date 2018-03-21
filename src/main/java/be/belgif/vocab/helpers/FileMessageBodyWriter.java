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
package be.belgif.vocab.helpers;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.file.Files;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyWriter;

import org.eclipse.rdf4j.rio.RDFFormat;

/**
 * Static file Writer
 *
 * @author Bart Hanssens
 */
@Produces({MediaType.APPLICATION_XML + ";charset=utf-8", 
	MediaType.TEXT_XML + ";charset=utf-8",
	RDFMediaType.JSONLD + ";charset=utf-8",
	RDFMediaType.NTRIPLES + ";charset=utf-8",
	RDFMediaType.TURTLE + ";charset=utf-8"})
public class FileMessageBodyWriter implements MessageBodyWriter<File> {

	@Override
	public boolean isWriteable(Class<?> type, Type generic, Annotation[] antns, MediaType mt) {
		return generic == File.class;
	}

	@Override
	public long getSize(File f, Class<?> type, Type generic, Annotation[] antns, MediaType mt) {
		return -1; // ignored by Jersey 2.0 anyway
	}

	@Override
	public void writeTo(File f, Class<?> type, Type generic, Annotation[] antns, MediaType mt,
			MultivaluedMap<String, Object> headers, OutputStream out)
			throws IOException, WebApplicationException {

		// check if there is a file extension
		if (! f.getName().contains(".")) {
			RDFFormat fmt = RDFMediaType.getRDFFormat(mt);
			if (fmt == null) {
				throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
			}
			f = new File(f.toString() + "." + fmt.getDefaultFileExtension());
		}
		
		if (! Files.isRegularFile(f.toPath())){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		try {
			Files.copy(f.toPath(), out);
		} catch (IOException ex) {
			throw new WebApplicationException(ex);
		}
	}
}
