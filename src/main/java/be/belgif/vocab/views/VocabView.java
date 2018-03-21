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
package be.belgif.vocab.views;

import be.belgif.vocab.dao.SkosDAO;

import java.util.Iterator;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;

/**
 * HTML view for SKOS concept schema
 * 
 * @author Bart.Hanssens
 */
public class VocabView extends RdfView {
	private final SkosDAO term;
	private final String vocabName;
	
	/**
	 * Get the properties of a term
	 * 
	 * @return 
	 */
	public SkosDAO getTerm() {
		return this.term;
	}
	
	/**
	 * Get the name of the vocabulary
	 * 
	 * @return 
	 */
	public String getVocabName() {
		return this.vocabName;
	}
	
	/** 
	 * Constructor
	 * 
	 * @param vocab vocabulary name
	 * @param m triples
	 * @param lang language
	 */
	public VocabView(String vocab, Model m, String lang) {
		super(m.isEmpty() ? "notfound.ftl" : "vocab.ftl", lang);
		
		Iterator<Resource> i = m.subjects().iterator();
		this.term = i.hasNext() ? new SkosDAO(m, (IRI) i.next()) : null;
		this.vocabName = vocab;
	}
}

