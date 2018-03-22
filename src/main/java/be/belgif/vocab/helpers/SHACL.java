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
package be.belgif.vocab.helpers;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleNamespace;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;


/**
 * Constants for the Shapes Constraint Language.
 * Not needed anymore when RDF4J 2.3 will be finalized.
 *
 * @see <a href="https://www.w3.org/TR/2017/WD-shacl-20170303/">Shapes Constraint Language</a>
 *
 * @author Bart Hanssens
 */
public class SHACL {
	/**
	 * The SHACL namespace: http://www.w3.org/ns/shacl#
	 */
	public static final String NAMESPACE = "http://www.w3.org/ns/shacl#";

	/**
	 * Recommended prefix for the namespace: "sh"
	 */
	public static final String PREFIX = "sh";

	public static final IRI CLASS;
	public static final IRI DATATYPE;
	public static final IRI MAX_COUNT;
	public static final IRI MIN_COUNT;
	public static final IRI NODE_SHAPE;
	public static final IRI PATH;
	public static final IRI PROPERTY_SHAPE;
	public static final IRI PROPERTY;
	public static final IRI TARGET_CLASS;
	
	/**
	 * An immutable {@link Namespace} constant that represents the namespace.
	 */
	public static final Namespace NS = new SimpleNamespace(PREFIX, NAMESPACE);
	
	static {
		ValueFactory factory = SimpleValueFactory.getInstance();

		
		NODE_SHAPE = factory.createIRI(NAMESPACE, "NodeShape");
		PROPERTY_SHAPE = factory.createIRI(NAMESPACE, "PropertyShape");
		
		CLASS = factory.createIRI(NAMESPACE, "class");
		DATATYPE = factory.createIRI(NAMESPACE, "datatype");
		MAX_COUNT = factory.createIRI(NAMESPACE, "maxCount");
		MIN_COUNT = factory.createIRI(NAMESPACE, "minCount");
		PATH = factory.createIRI(NAMESPACE, "path");		
		PROPERTY = factory.createIRI(NAMESPACE, "property");
		TARGET_CLASS = factory.createIRI(NAMESPACE, "targetClass");
	}
}