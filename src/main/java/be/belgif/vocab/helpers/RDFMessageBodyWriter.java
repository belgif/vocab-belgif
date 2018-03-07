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

import be.belgif.vocab.ldf.Hydra;
import com.google.common.net.HttpHeaders;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.Rio;

/**
 * RDF Writer
 *
 * @author Bart.Hanssens
 */
@Provider
@Produces({RDFMediaType.JSONLD + ";charset=utf-8",
	RDFMediaType.NTRIPLES + ";charset=utf-8",
	RDFMediaType.TTL + ";charset=utf-8",
	RDFMediaType.TRIG + ";charset=utf-8"})
public class RDFMessageBodyWriter implements MessageBodyWriter<Model> {

	@Override
	public boolean isWriteable(Class<?> type, Type generic, Annotation[] antns, MediaType mt) {
		return generic == Model.class;
	}

	@Override
	public long getSize(Model m, Class<?> type, Type generic, Annotation[] antns, MediaType mt) {
		return -1; // ignored by Jersey 2.0 anyway
	}

	@Override
	public void writeTo(Model m, Class<?> type, Type generic, Annotation[] antns, MediaType mt,
			MultivaluedMap<String, Object> headers, OutputStream out)
			throws IOException, WebApplicationException {
		if (m.isEmpty()) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		RDFFormat fmt = RDFMediaType.getRDFFormat(mt);

		try {
			if (m.contains(null, Hydra.MAPPING, null)) {
				headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
			}
			Rio.write(m, out, fmt);
		} catch (RDFHandlerException ex) {
			throw new WebApplicationException(ex);
		}
	}
}
