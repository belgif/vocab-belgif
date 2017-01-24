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
package be.belgif.vocab.dao;

import java.util.Set;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.eclipse.rdf4j.model.vocabulary.VOID;

/**
 * DAO helper class for VoID.
 * 
 * @author Bart.Hanssens
 */
public class SkosDAO extends RdfDAO {

	/**
	 * Get SKOS notation
	 * 
	 * @return string 
	 */
	public String getNotation() {
		return literal(SKOS.NOTATION);
	}
	
	/**
	 * Get list of SKOS broader
	 * 
	 * @return uri as string 
	 */
	public Set<Value> getBroader() {
		return objs(SKOS.BROADER);
	}

	/**
	 * Get list of SKOS narrower
	 * 
	 * @return uri as string 
	 */
	public Set<Value> getNarrower() {
		return objs(SKOS.NARROWER);
	}
	
	/**
	 * Get list of SKOS exactMatches
	 * 
	 * @return set of uri
	 */
	public Set<Value> getExactMatch() {
		return objs(SKOS.EXACT_MATCH);
	}
	
	
	/**
	 * Get download URL
	 * 
	 * @return download URL
	 */
	public String getDownload() {
		return obj(VOID.DATA_DUMP).toString();
	}
	
	/**
	 * Constructor
	 * 
	 * @param m triples
	 * @param id subject ID
	 */
	public SkosDAO(Model m, IRI id) {
		super(m, id);
	}
}