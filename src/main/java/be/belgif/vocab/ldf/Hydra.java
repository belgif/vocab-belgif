/*
 * Copyright (c) 2016, Bart Hanssens <bart.hanssens@bosa.fgov.be>
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
package be.belgif.vocab.ldf;


import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;


/**
 * vCard RDF helper class
 * 
 * @author Bart Hanssens <bart.hanssens@bosa.fgov.be>
 */
public class Hydra {
	public final static String NAMESPACE = "http://www.w3.org/ns/hydra/core#";
	public final static String PREFIX = "hydra";
		
	public final static IRI COLLECTION;
	public final static IRI FIRST;
	public final static IRI ITEMS;
	public final static IRI MAPPING;
	public final static IRI MEMBER;
	public final static IRI NEXT;
	public final static IRI PARTIAL;
	public final static IRI PREVIOUS;
	public final static IRI PROPERTY;
	public final static IRI SEARCH;
	public final static IRI TEMPLATE;
	public final static IRI TOTAL;
	public final static IRI VARIABLE;
	public final static IRI VIEW;
	
		
	static {
		ValueFactory f = SimpleValueFactory.getInstance();
		COLLECTION = f.createIRI(NAMESPACE, "Collection");
		FIRST = f.createIRI(NAMESPACE, "first");
		ITEMS = f.createIRI(NAMESPACE, "itemsPerPage");
		MAPPING = f.createIRI(NAMESPACE, "mapping");
		MEMBER = f.createIRI(NAMESPACE, "member");
		NEXT = f.createIRI(NAMESPACE, "next");
		PARTIAL = f.createIRI(NAMESPACE, "PartialCollectionView");
		PREVIOUS = f.createIRI(NAMESPACE, "previous");
		PROPERTY = f.createIRI(NAMESPACE, "property");
		SEARCH = f.createIRI(NAMESPACE, "search");
		TEMPLATE = f.createIRI(NAMESPACE, "template");
		TOTAL = f.createIRI(NAMESPACE, "totalItems");
		VARIABLE = f.createIRI(NAMESPACE, "variable");
		VIEW = f.createIRI(NAMESPACE, "view");
	}
}
