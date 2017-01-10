/*
 * Copyright (c) 2017, Bart Hanssens <bart.hanssens@fedict.be>
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

import io.dropwizard.views.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.ext.Provider;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.vocabulary.DCTERMS;

/**
 * HTML Writer
 * 
 * @author Bart.Hanssens
 */
@Provider
public class VocabListView extends View {
	private final ArrayList<Vocab> vocabs = new ArrayList();
	
	public class Vocab {
		private final String name; 
		private final HashMap<String,String> title = new HashMap();
		private final HashMap<String,String> desc = new HashMap(); 

		public String getName() {
			return name;
		}

		public String getTitle(String lang) {
			return title.getOrDefault(lang, "");
		}

		public void addTitle(String title, String lang) {
			this.title.put(lang, title);
		}

		public String getDesc(String lang) {
			return desc.getOrDefault(lang, "");
		}

		public void addDesc(String desc, String lang) {
			this.desc.put(lang, desc);
		}
				
		public Vocab(String name) {
			this.name = name;
		}
	}
	
	public List<Vocab> getVocabs() {
		return this.vocabs;
	}
	
	/** 
	 * Constructor
	 * 
	 * @param m triples
	 * @param lang language
	 */
	public VocabListView(Model m, String lang) {
		super("vocablist.ftl");
		
		m.subjects().forEach(s -> {
			Vocab v = new Vocab(s.toString());
			m.filter(s, DCTERMS.TITLE, null).forEach(t -> {
				Literal l = (Literal) t.getObject();
				v.addTitle(l.getLabel(), l.getLanguage().orElse(""));
			});
			m.filter(s, DCTERMS.DESCRIPTION, null).forEach(d -> {
				Literal l = (Literal) d.getObject();
				v.addDesc(l.getLabel(), l.getLanguage().orElse(""));
			});
			vocabs.add(v);
		});
	}
}

