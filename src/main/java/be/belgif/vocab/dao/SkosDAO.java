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
package be.belgif.vocab.dao;

import java.util.Set;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.eclipse.rdf4j.model.vocabulary.VOID;

/**
 * DAO helper class for SKOS vocabularies
 * Only used for HTML view
 * 
 * @author Bart Hanssens
 */
public class SkosDAO extends RdfDAO {
	/**
	 * Get SKOS root resource
	 *
	 * @return uri as string
	 */
	public String getRoot() {
		Value v = obj(VOID.ROOT_RESOURCE);
		return (v != null) ? v.toString() : "";
	}
	
	/**
	 * Get download URL for a full download of the vocabulary
	 *
	 * @return download URL as string
	 */
	public String getDownload() {
		return getId().toString().replaceFirst("/auth/", "/dataset/")
					.replaceFirst("#id", "");
	}
	
	/**
	 * Get SKOS notation
	 *
	 * @return string or empty string
	 */
	public String getNotation() {
		return literal(SKOS.NOTATION, "");
	}

	/**
	 * Get SKOS top concepts
	 * 
	 * @return set of IRIs
	 */
	public Set<Value> getTopConcepts() {
		return objs(SKOS.HAS_TOP_CONCEPT);
	}

	/**
	 * Get alternative labels in specific language
	 * 
	 * @param lang language code
	 * @return set of strings
	 */
	public Set<String> getAltLabels(String lang) {
		return literals(SKOS.ALT_LABEL, lang);
	}
	
	/**
	 * Get preferred labels in specific language
	 * 
	 * @param lang language code
	 * @return set of strings
	 */
	public Set<String> getPrefLabels(String lang) {
		return literals(SKOS.PREF_LABEL, lang);
	}
	
	/**
	 * Get broader matches
	 * 
	 * @return set of IRIs or empty set
	 */
	public Set<Value> getBroaders() {
		return objs(SKOS.BROADER);
	}
	
	/**
	 * Get narrower matches
	 * 
	 * @return set of IRIs or empty set
	 */
	public Set<Value> getNarrowers() {
		return objs(SKOS.NARROWER);
	}
	
	
	/**
	 * Get close matches
	 * 
	 * @return set of IRIs or empty set
	 */
	public Set<Value> getCloseMatches() {
		return objs(SKOS.CLOSE_MATCH);
	}
	
	/**
	 * Get exact matches
	 * 
	 * @return set of IRIs or empty set
	 */
	public Set<Value> getExactMatches() {
		return objs(SKOS.EXACT_MATCH);
	}
	
	/**
	 * Get broad matches
	 * 
	 * @return set of IRIs or empty set
	 */
	public Set<Value> getBroadMatches() {
		return objs(SKOS.BROAD_MATCH);
	}
	
	/**
	 * Get narrow matches
	 * 
	 * @return set of IRIs or empty set
	 */
	public Set<Value> getNarrowMatches() {
		return objs(SKOS.NARROW_MATCH);
	}
	
	
	/**
	 * Constructor
	 *
	 * @param m triples
	 * @param id subject ID
	 */
	public SkosDAO(Model m, Resource id) {
		super(m, id);
	}
}
