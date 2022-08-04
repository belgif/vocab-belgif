/*
 * Copyright (c) 2022, FPS BOSA
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


import be.belgif.vocab.dao.LicenseDAO;
import be.belgif.vocab.dao.OrgDAO;
import org.eclipse.rdf4j.model.Model;

/**
 * HTML view for SKOS concept schema
 * 
 * @author Bart.Hanssens
 */
public abstract class RdfInfoView extends RdfView {
	private final LicenseDAO license;
	private final OrgDAO org;
	
	/**
	 * Get license info
	 * @return 
	 */
	public LicenseDAO getLicense() {
		return this.license;
	}

	/**
	 * Get organization info
	 * 
	 * @return 
	 */
	public OrgDAO getOrganization() {
		return this.org;
	}

	/** 
	 * Constructor
	 * 
	 * @param template template file
	 * @param lang language
	 * @param org
	 * @param license
	 */
	protected RdfInfoView(String template, String lang, Model org, Model license) {
		super(template, lang);

		this.org = (org == null || org.isEmpty()) 
			? null
			: new OrgDAO(org, org.subjects().iterator().next());
		this.license = (license == null || license.isEmpty()) 
			? null
			: new LicenseDAO(license, license.subjects().iterator().next());
	}
}

