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

import javax.ws.rs.core.MediaType;
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
	public final static String TTL = "text/turtle";
	
	/**
	 * Get RDF Format from mediatype
	 * 
	 * @param mt Jersey media type
	 * @return RDF4J rdf format 
	 */
	public static RDFFormat getRDFFormat(MediaType mt) {
		RDFFormat fmt;
	
		// check for content type, ignoring the charset
		switch(mt.getType() + "/" + mt.getSubtype()) {
			case RDFMediaType.NTRIPLES: fmt = RDFFormat.NTRIPLES; break;
			case RDFMediaType.TTL: fmt = RDFFormat.TURTLE; break;
			case RDFMediaType.TRIG: fmt = RDFFormat.TRIG; break;
			default: fmt = RDFFormat.JSONLD; break;
		}
		return fmt;
	}
}
