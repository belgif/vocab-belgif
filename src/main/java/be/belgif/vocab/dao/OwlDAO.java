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
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;

/**
 * DAO helper class for ontology overview.
 * Only used for HTML view
 *
 * @author BartHanssens
 */
public class OwlDAO extends RdfDAO {
	private final List<OwlThingDAO> classes = new ArrayList<>();
	private final List<OwlThingDAO> properties = new ArrayList<>();
	
	/**
	 * Helper class for OWL classes and properties
	 */
	public class OwlThingDAO extends RdfDAO {
		/**
		 * Get domains
		 *
		 * @return set of IRIs or empty set
		 */
		public Set<Value> getDomains() {
			return objs(RDFS.DOMAIN);
		}

		/**
		 * Get ranges
		 *
		 * @return set of IRIS or empty set
		 */
		public Set<Value> getRanges() {
			return objs(RDFS.RANGE);
		}

		/**
		 * Get subclasses
		 *
		 * @return set of IRIS or empty set
		 */
		public Set<Value> getSubClasses() {
			return objs(RDFS.SUBCLASSOF);
		}

		/**
		 * Get subproperties
		 *
		 * @return set of IRIS or empty set
		 */
		public Set<Value> getSubProperties() {
			return objs(RDFS.SUBPROPERTYOF);
		}

		/**
		 * Constructor
		 * 
		 * @param m model
		 * @param id subject IRI
		 */
		public OwlThingDAO(Model m, Resource id) {
			super(m, id);
		}
	}
		
	/**
	 * Get version info in a specific language
	 * 
	 * @param lang language code
	 * @return string or empty string
	 */
	public String getVersion(String lang) {
		return literal(OWL.VERSIONINFO, lang);
	}
	
	
	/**
	 * Sort a list of properties or classes and group by starting letter.
	 * 
	 * @param lst list of properties or classes
	 * @return sorted nested map
	 */
	private SortedMap<String,SortedSet<String>> getLetter(List<OwlThingDAO> lst) {
		TreeMap<String,SortedSet<String>> map = new TreeMap<>();
		
		for (RdfDAO rdf: lst) {
			String name = ((IRI) rdf.getId()).getLocalName();
			String letter = name.substring(0, 1);
			SortedSet set = map.get(letter);
			
			if (set == null) {
				set = new TreeSet<>();
				map.put(letter, set);
			}
			set.add(name);
		}
		return map;
	}
	
	/**
	 * Get RDFS/OWL classes
	 * 
	 * @return list of classes
	 */
	public List<OwlThingDAO> getClasses() {
		return this.classes;
	}

	/**
	 * Get ordered list of class names, grouped by starting letter.
	 * 
	 * @return nested map
	 */
	public SortedMap<String,SortedSet<String>> getClassesLetter() {
		return getLetter(getClasses());
	}
	
	/**
	 * Get properties
	 * 
	 * @return list of properties
	 */
	public List<OwlThingDAO> getProperties() {
		return this.properties;
	}
	
	/**
	 * Get ordered list of property names, grouped by starting letter.
	 * 
	 * @return nested map
	 */
	public SortedMap<String,SortedSet<String>> getPropertiesLetter() {
		return getLetter(getProperties());
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
			classes.add(new OwlThingDAO(getModel(), (IRI) subj));
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
			properties.add(new OwlThingDAO(getModel(), (IRI) subj));
		}
	}
	
 	/**
	 * Constructor
	 *
	 * @param m triples
	 * @param id subject ID
	 */
	public OwlDAO(Model m, Resource id) {
		super(m, id);
		initClasses(m);
		initProperties(m);
	}
}
