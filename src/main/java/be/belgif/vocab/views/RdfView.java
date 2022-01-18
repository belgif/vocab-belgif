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
package be.belgif.vocab.views;

import io.dropwizard.views.View;

import java.nio.charset.StandardCharsets;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * HTML view for SKOS concept schema
 * 
 * @author Bart.Hanssens
 */
public abstract class RdfView extends View {
	private final String lang;
	private final ResourceBundle msgs;
	
	/**
	 * Get the language code
	 * 
	 * @return language code
	 */
	public String getLang() {
		return this.lang;
	}
	
	/**
	 * Get the message bundle
	 * 
	 * @return resource bundle
	 */
	public ResourceBundle getMessages() {
		return this.msgs;
	}
	
	/** 
	 * Constructor
	 * 
	 * @param template template file
	 * @param lang language
	 */
	protected RdfView(String template, String lang) {
		super(template, StandardCharsets.UTF_8);
		this.lang = (lang == null || lang.isEmpty()) ? "en" : lang;
		this.msgs = ResourceBundle.getBundle("be.belgif.vocab.views.MessageBundle", 
							new Locale(this.lang));
	}
}

