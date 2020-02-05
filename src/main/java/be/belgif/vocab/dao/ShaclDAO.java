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

import be.belgif.vocab.App;
import be.belgif.vocab.helpers.QueryHelper;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.SHACL;

/**
 * DAO helper class for SHACL shapes.
 * Only used for HTML view
 *
 * @author Bart Hanssens
 */
public class ShaclDAO extends RdfDAO {
	private final String vocabNs = App.getPrefix() + "ns/";
	
	private final List<ShaclNodeShapeDAO> shapes = new ArrayList<>();
	private final BiMap<String,String> usedNs = HashBiMap.create();
	
	
	/**
	 * PropertyShape helper class
	 */
	public class ShaclPropertyShapeDAO extends RdfDAO {
		private final List<ShaclPropertyShapeDAO> nested = new ArrayList<>();
		
		/**
		 * Get path
		 * 
		 * @return string
		 */
		public Value getPath() {
			Value v = obj(SHACL.PATH);
			return (v != null) ? v : QueryHelper.asLiteral("");
		}
		
		/**
		 * Get class
		 * 
		 * @return string
		 */
		public Value getShClass() {
			return obj(SHACL.CLASS);
		}
		
		/**
		 * Get prefixed data type
		 * 
		 * @return string
		 */
		public Value getDataType() {
			return obj(SHACL.DATATYPE);
		}

		/**
		 * Get has value object, e.g. for skos:inSchema
		 * 
		 * @return value
		 */
		public Value getHasValue() {
			return obj(SHACL.HAS_VALUE);
		}
		
		/**
		 * Get max count value
		 * 
		 * @return 
		 */
		public String getMaxCount() {
			return literal(SHACL.MAX_COUNT, "");
		}
		
		/**
		 * Get min count value
		 * @return 
		 */
		public String getMinCount() {
			return literal(SHACL.MIN_COUNT, "");
		}
		
		/**
		 * Get list of language codes
		 * 
		 * @return set of values or empty set
		 */
		public Set<Value> getLangIn() {
			return collection(SHACL.LANGUAGE_IN, getModel());
		}
		
		/**
		 * Get unique lang restriction
		 * 
		 * @return 
		 */
		public String getUniqueLang() {
			return literal(SHACL.UNIQUE_LANG, "");
		} 
		
		/**
		 * Get list of nested property shapes
		 * 
		 * @return 
		 */
		public List<ShaclPropertyShapeDAO> getPropertyShapes() {
			return nested;
		}
		
		/**
		 * Initialize nested property shapes
		 * 
		 * @param parent
		 */
		private void initPropertyShapes(Resource parent) {
			Set<Value> nodes = getModel().filter(parent, SHACL.NODE, null).objects();

			for(Value node: nodes) {
				if (! (node instanceof Resource)) {
					continue;
				}
				
				Set<Value> props = getModel().filter((Resource) node, SHACL.PROPERTY, null).objects();
				for (Value prop: props) {
					if (! (prop instanceof Resource)) {
						continue;
					}
					nested.add(new ShaclPropertyShapeDAO(getModel(), (Resource) prop));
				}
			}
		}

		/**
		 * Constructor
		 * 
		 * @param m model
		 * @param id 
		 */
		public ShaclPropertyShapeDAO(Model m, Resource id) {
			super(m, id);
			// nested property shapes
			initPropertyShapes(id);	
		}
	}
	
	/**
	 * NodeShape helper class
	 */
	public class ShaclNodeShapeDAO extends RdfDAO {
		private final List<ShaclPropertyShapeDAO> shapes = new ArrayList<>();
		
		/**
		 * Get prefixed target class
		 * 
		 * @return 
		 */
		public Value getTarget() {
			return obj(SHACL.TARGET_CLASS);
		}
		
		/**
		 * Initialize SHACL property shapes
		 * 
		 * @param node parent node shape
		 */
		private void initPropertyShapes(Resource node) {
			Set<Value> subjs = new HashSet<>();
			subjs.addAll(getModel().filter(node, SHACL.PROPERTY, null).objects());

			for(Value subj: subjs) {
				if (! (subj instanceof Resource)) {
					continue;
				}
				shapes.add(new ShaclPropertyShapeDAO(getModel(), (Resource) subj));
			}
		}
	
		/**
		 * Get list of property shapes
		 * 
		 * @return 
		 */
		public List<ShaclPropertyShapeDAO> getPropertyShapes() {
			return shapes;
		}

		/**
		 * Constructor
		 * 
		 * @param m triples
		 * @param id
		 */
		public ShaclNodeShapeDAO(Model m, Resource id) {
			super(m, id);
			initPropertyShapes(id);
		}
	}
	
	
	/**
	 * Get list of SHACL node shapes
	 * 
	 * @return 
	 */
	public List<ShaclNodeShapeDAO> getShapes() {
		return this.shapes;
	}
	
	/**
	 * Get map of used namespaces
	 * 
	 * @return map of <namespace,prefix> 
	 */
	public Map<String,String> getUsedNamespaces() {
		return this.usedNs.inverse();
	}
	
	/**
	 * Get short (prefixed) name of an IRI
	 * 
	 * @param val full IRI value
	 * @return string
	 */
	public String getShort(Value val) {
		if (! (val instanceof IRI)) {
			return "";
		}
		IRI iri = (IRI) val;
		String prefix = usedNs.get(iri.getNamespace());
		
		return (prefix == null) ? val.toString() 
					: prefix + ":" + iri.getLocalName();	
	}
	
	/**
	 * Get short (prefixed) name of an IRI
	 * 
	 * @param iri full IRI value
	 * @return string
	 */
	public String getShort(String iri) {
		return getShort(QueryHelper.asURI(iri));
	}
	
	/**
	 * Initialize SHACL property shapes
	 */
	private void initNodeShapes() {
		Set<Resource> subjs = getModel().filter(null, RDF.TYPE, SHACL.NODE_SHAPE).subjects();
		Set<Resource> nested = getModel().filter(null, SHACL.NODE, null).objects()
									.stream().map(o -> (Resource) o)
									.collect(Collectors.toSet());
		subjs.removeAll(nested);
		for(Resource subj: subjs) {
			shapes.add(new ShaclNodeShapeDAO(getModel(), (Resource) subj));
		}
	}
	
	/**
	 * Initialize a map of "known" namespaces and their prefixes used in this SHACL
	 */
	private void initUsedNamespaces() {
		Set<String> ns = new HashSet<>();
		
		// only check objects (e.g. targetClass, datatype)
		for (Value val: getModel().objects()) {
			if (! (val instanceof IRI)) {
				continue;
			}
			ns.add(((IRI) val).getNamespace());
		}
		// Ignore SHACL
		ns.remove(SHACL.NAMESPACE);
		
		// get <namespace,prefix> instead of <prefix,namespace>
		BiMap<String,String> inverseNS = QueryHelper.NS_MAP.inverse();
			
		for (String n: ns) {
			// "local" namespace published on vocab
			if (n.startsWith(vocabNs)) {
				usedNs.put(n, n.replaceFirst(vocabNs, "be-").replace("#", ""));
			} else {
				String prefix = inverseNS.get(n);
				if (prefix != null) {
					usedNs.put(n, prefix);
				}
			}
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
	 * Constructor
	 *
	 * @param m triples
	 * @param id subject ID
	 */
	public ShaclDAO(Model m, Resource id) {
		super(m, id);
		initUsedNamespaces();
		initNodeShapes();
	}
}
