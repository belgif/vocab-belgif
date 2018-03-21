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

import be.belgif.vocab.helpers.SHACL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.vocabulary.RDF;

/**
 * DAO helper class for SHACL shapes.
 *
 * @author Bart Hanssens
 */
public class ShaclDAO extends RdfDAO {
	/**
	 * SHACL node shape
	 */
	public class ShaclNodeDAO extends RdfDAO {
		private final List<RdfDAO> shapes = new ArrayList<>();
		
		/**
		 * Initialize SHACL property shapes
		 * 
		 * @param m triples
		 * @param node parent node shape
		 */
		private void initPropertyShapes(Model m, Resource node) {
			Set<Value> subjs = new HashSet<>();
			subjs.addAll(m.filter(node, SHACL.PROPERTY, null).objects());

			for(Value subj: subjs) {
				if (! (subj instanceof Resource)) {
					continue;
				}
				Model mp = new LinkedHashModel();
				m.getNamespaces().forEach(mp::setNamespace);
				mp.addAll(m.filter((Resource) subj, null, null));
				shapes.add(new RdfDAO(mp, (Resource) subj));
				m.removeAll(mp);
			}
		}

	
		/**
		 * Get list of propertyshapes
		 * 
		 * @return 
		 */
		public List<RdfDAO> getPropertyShapes() {
			return shapes;
		}

		/**
		 * Constructor
		 * 
		 * @param m triples
		 * @param id
		 * @param fullm complete model 
		 */
		public ShaclNodeDAO(Model m, Resource id, Model fullm) {
			super(m, id);
			initPropertyShapes(fullm, id);
		}
	}

	private final List<RdfDAO> shapes = new ArrayList<>();

	/**
	 * Get list of SHACL node shapes
	 * 
	 * @return 
	 */
	public List<RdfDAO> getShapes() {
		return this.shapes;
	}
	
	/**
	 * Initialize SHACL property shapes
	 * 
	 * @param m 
	 */
	private void initNodeShapes(Model m) {
		Set<Resource> subjs = new HashSet<>();
		subjs.addAll(m.filter(null, RDF.TYPE, SHACL.NODE_SHAPE).subjects());
		
		for(Resource subj: subjs) {
			Model mp = new LinkedHashModel();
			m.getNamespaces().forEach(mp::setNamespace);
			mp.addAll(m.filter(subj, null, null));
			shapes.add(new ShaclNodeDAO(mp, (Resource) subj, m));
			m.removeAll(mp);
		}
	}
	
 	/**
	 * Constructor
	 *
	 * @param m triples
	 * @param id subject ID
	 */
	public ShaclDAO(Model m, Resource id) {
		super(m, id);
		initNodeShapes(m);
	}
}
