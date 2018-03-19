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
package be.belgif.vocab.views;

import be.belgif.vocab.dao.OntoDAO;
import be.belgif.vocab.dao.OwlDAO;

import java.util.Iterator;

import javax.ws.rs.ext.Provider;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;

/**
 * HTML view for SKOS concept schema
 * 
 * @author Bart.Hanssens
 */
@Provider
public class OwlView extends RdfView {
	private final OwlDAO onto;
	
	/**
	 * Get ontology
	 * 
	 * @return 
	 */
	public OwlDAO getOnto() {
		return this.onto;
	}
	
	/** 
	 * Constructor
	 * 
	 * @param onto ontology name name
	 * @param m triples
	 * @param lang language
	 */
	public OwlView(String onto, Model m, String lang) {
		super(m.isEmpty() ? "notfound.ftl" : "onto.ftl", lang);
		m.filter(null, RDF.TYPE, OWL.ONTOLOGY);
		Iterator<Resource> i = m.subjects().iterator();
		this.onto = i.hasNext() ? new OwlDAO(m, (IRI) i.next()) : null;
	}
}

