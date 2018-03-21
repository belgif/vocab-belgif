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

import be.belgif.vocab.dao.RdfDAO;
import be.belgif.vocab.dao.XmlnsDAO;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ext.Provider;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;

/**
 * HTML view for the list of RDFS/OWL ontologies
 * 
 * @author Bart Hanssens
 */
@Provider
public class OntoListView extends RdfView {
	private final List<XmlnsDAO> xmlns = new ArrayList();
	private final List<RdfDAO> ontos = new ArrayList();	

	/**
	 * Get the list of ontologies
	 * 
	 * @return list
	 */
	public List<RdfDAO> getOntos() {
		return this.ontos;
	}
	
	/**
	 * Get the list of XSD namespaces
	 * 
	 * @return list
	 */
	public List<XmlnsDAO> getXmlns() {
		return this.xmlns;
	}
	
	/** 
	 * Constructor
	 * 
	 * @param mx XSD namespaces as triples
	 * @param mo ontologies as triples
	 * @param lang language
	 */
	public OntoListView(Model mx, Model mo, String lang) {
		super("ontolist.ftl", lang);
		
		mx.subjects().stream().forEachOrdered(subj -> {
			Model m = new LinkedHashModel();
			mx.getNamespaces().forEach(m::setNamespace);
			m.addAll(mx.filter(subj, null, null));
			xmlns.add(new XmlnsDAO(m, (IRI) subj));
		});
		
		mo.subjects().stream().filter(s -> !s.toString().contains("shacl"))
					.forEachOrdered(subj -> {
			Model m = new LinkedHashModel();
			mo.getNamespaces().forEach(m::setNamespace);
			m.addAll(mo.filter(subj, null, null));
			ontos.add(new RdfDAO(m, subj));
		});
	}
}