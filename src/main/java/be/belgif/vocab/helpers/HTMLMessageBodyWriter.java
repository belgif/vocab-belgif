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
package be.belgif.vocab.helpers;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Set;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.Charsets;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;

/**
 * HTML Writer
 * 
 * @author Bart.Hanssens
 */
@Provider
@Produces(MediaType.TEXT_HTML + ";charset=utf-8")
public class HTMLMessageBodyWriter implements MessageBodyWriter<Model> {
	@Override
	public boolean isWriteable(Class<?> type, Type generic, Annotation[] antns, MediaType mt) {
		return generic == Model.class;
	}

	@Override
	public long getSize(Model m, Class<?> type, Type generic, Annotation[] antns, MediaType mt) {
		return -1; // ignored by Jersey 2.0 anyway
	}

	private String shorten(Set<Namespace> ns, IRI pred) {
		String pref = "";
		for(Namespace n: ns) {
			if (n.getName().equals(pred.getNamespace())) {
				pref = n.getPrefix();
				break;
			}
		}
		if (! pref.isEmpty()) {
			return pref + ":" + pred.getLocalName();
		}
		return pred.stringValue();
	}
	
	private String link(Value val) {
		String href = val.stringValue();
		if (val instanceof Resource) {
			if (href.contains("belgif.be")) {
				href = "<a href='" + href + "'>" + href + "</a>";
			}
		}
		return href;
	}
		
	@Override
	public void writeTo(Model m, Class<?> type, Type generic, Annotation[] antns, MediaType mt, 
										MultivaluedMap<String, Object> mm, OutputStream out) 
									throws IOException, WebApplicationException {
		StringBuilder builder = new StringBuilder();
		builder.append("<!DOCTYPE html>\n")
				.append("<html>\n")
				.append("<head>\n")
				.append("<meta charset='UTF-8'>\n")
				.append("<title></title>\n")
				.append("</head>\n")
				.append("<body>\n");
		

		builder.append("<table>\n")
				.append("<tr><th>Namespace</th><th><tr>Prefix</tr>");
		Set<Namespace> ns = m.getNamespaces();

		for (Namespace n: ns) {
			builder.append("<tr><td>")
					.append(n.getName()).append("</td><td>")
					.append(n.getPrefix()).append("</td><td>\n");
		}
		builder.append("</table>\n");
		
		builder.append("<table>\n").
				append("<tr><th>Subject</th><th>Predicate</th><th>Object<th></tr>\n");
		
		Iterator<Statement> iter = m.iterator();
		while(iter.hasNext()) {
			Statement stmt = iter.next();
			builder.append("<tr><td>")
					.append(stmt.getSubject()).append("</td><td>")
					.append(shorten(ns, stmt.getPredicate())).append("</td><td>");
			builder.append(link(stmt.getObject())).append("</td></tr>\n");
		}
		builder.append("</table>\n");
		
		builder.append("</body>\n")
				.append("</html>");
		
		out.write(builder.toString().getBytes(Charsets.UTF_8));
		out.flush();
	}
}
