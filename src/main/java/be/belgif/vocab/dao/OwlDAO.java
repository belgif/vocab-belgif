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
package be.belgif.vocab.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;

/**
 * DAO helper class for ontology overview.
 *
 * @author Bart.Hanssens
 */
public class OwlDAO extends RdfDAO {
	private final List<RdfDAO> classes = new ArrayList<>();
	private final List<RdfDAO> properties = new ArrayList<>();
	
	/**
	 * Get RDFS/OWL classes
	 * 
	 * @return 
	 */
	public List<RdfDAO> getClasses() {
		return this.classes;
	}
	
	/**
	 * Get RDFS/OWL properties
	 * 
	 * @return 
	 */
	public List<RdfDAO> getProperties() {
		return this.properties;
	}
	
	/**
	 * Initialize RDF/OWL classes
	 * 
	 * @param m 
	 */
	private void initClasses(Model m) {
		// Get all classes and subclasses, without duplicates
		Set<Resource> subjs = new HashSet<>();
		subjs.addAll(m.filter(null, RDF.TYPE, RDFS.CLASS).subjects());
		subjs.addAll(m.filter(null, RDFS.SUBCLASSOF, null).subjects());
		
		for(Resource subj: subjs) {
			// First copy the results to a new model, otherwise remove will fail
			Model mc = new LinkedHashModel();
			m.getNamespaces().forEach(mc::setNamespace);
			mc.addAll(m.filter(subj, null, null));
			classes.add(new RdfDAO(mc, (IRI) subj));
			m.removeAll(mc);
		}
	}
	
	/**
	 * Initialize RDF/OWL properties
	 * 
	 * @param m 
	 */
	private void initProperties(Model m) {
		// Get all properties and subproperies, without duplicates
		Set<Resource> subjs = new HashSet<>();
		subjs.addAll(m.filter(null, RDF.TYPE, RDF.PROPERTY).subjects());
		subjs.addAll(m.filter(null, RDFS.SUBPROPERTYOF, null).subjects());

		for(Resource subj: subjs) {
			Model mp = new LinkedHashModel();
			m.getNamespaces().forEach(mp::setNamespace);
			mp.addAll(m.filter(subj, null, null));
			properties.add(new RdfDAO(mp, (IRI) subj));
			m.removeAll(mp);
		}
	}
	
 	/**
	 * Constructor
	 *
	 * @param m triples
	 * @param id subject ID
	 */
	public OwlDAO(Model m, IRI id) {
		super(m, id);
		initClasses(m);
		initProperties(m);
	}
}
