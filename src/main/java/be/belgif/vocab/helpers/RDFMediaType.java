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

import java.util.HashSet;
import java.util.Set;

import jakarta.ws.rs.core.MediaType;

import org.eclipse.rdf4j.rio.RDFFormat;

/**
 * Helper class for RDF serialization types
 *
 * @author Bart.Hanssens
 */
public class RDFMediaType {
	// can't use RDFFormat.xyz.toString(): not constant
	public final static String JSONLD = "application/ld+json";
	public final static String NTRIPLES = "application/n-triples";
	public final static String TRIG = "application/trig";
	public final static String TURTLE = "text/turtle";

	private final static Set<RDFFormat> FMTS = new HashSet<>();
	static {
		FMTS.add(RDFFormat.JSONLD);
		FMTS.add(RDFFormat.NTRIPLES);
		FMTS.add(RDFFormat.TRIG);
		FMTS.add(RDFFormat.TURTLE);
	}
		
	/**
	 * Get RDF format from file extension
	 * @param ext file extension
	 * @return RDFFormat or null
	 */
	public static RDFFormat getRDFFormat(String ext) {
		for(RDFFormat fmt: FMTS) {
			if (fmt.hasFileExtension(ext)) {
				return fmt;
			}
		}
		return null;
	}
	
	/**
	 * Get RDF Format from mediatype
	 *
	 * @param mt Jersey media type
	 * @return RDFFormat or null
	 */
	public static RDFFormat getRDFFormat(MediaType mt) {
		String mime = mt.getType() + "/" + mt.getSubtype();
		for (RDFFormat fmt: FMTS) {
			if (fmt.hasMIMEType(mime)) {
				return fmt;
			}
		}
		return null;
	}
}
