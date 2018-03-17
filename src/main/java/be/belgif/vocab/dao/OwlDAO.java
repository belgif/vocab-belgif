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
import java.util.List;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
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
	
	public List<RdfDAO> getClasses() {
		return this.classes;
	}
	
	public List<RdfDAO> getProperties() {
		return this.properties;
	}
	
	/**
	 * Initialize classes
	 * 
	 * @param m 
	 */
	private void initClasses(Model m) {
		Model mc = m.filter(null, RDF.TYPE, RDFS.CLASS);
		mc.addAll(m.filter(null, RDF.TYPE, RDFS.SUBCLASSOF));
		
		mc.subjects().forEach(c -> classes.add(new RdfDAO(mc, (IRI) c)));
	}
	
	/**
	 * Initialize properties
	 * 
	 * @param m 
	 */
	private void initProperties(Model m) {
		Model mp = m.filter(null, RDF.TYPE, RDF.PROPERTY);
		mp.addAll(m.filter(null, RDF.TYPE, RDFS.SUBPROPERTYOF));

		mp.subjects().forEach(p -> properties.add(new RdfDAO(mp, (IRI) p)));
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
