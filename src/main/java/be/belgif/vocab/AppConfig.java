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
package be.belgif.vocab;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;

import io.dropwizard.Configuration;
import io.dropwizard.bundles.assets.AssetsBundleConfiguration;
import io.dropwizard.bundles.assets.AssetsConfiguration;

import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Bart.Hanssens
 */
public class AppConfig extends Configuration implements AssetsBundleConfiguration {
/*	@NotEmpty
	private String server;
	
	@NotEmpty
	private String repo;
*/
	@NotEmpty
	private String dataDir;
	
	@NotEmpty
	private String luceneDir;
			
	@NotEmpty
	private String vocabImportDir;

	@NotEmpty
	private String vocabDownloadDir;

	@NotEmpty
	private String xsdImportDir;

	@NotEmpty
	private String xsdDownloadDir;

	@NotEmpty
	private String sitePrefix;

	private ImmutableMap<String, Map<String, String>> views;

	@Valid
	@NotNull
	@JsonProperty
	private final AssetsConfiguration assets = AssetsConfiguration.builder().build();

	@JsonProperty
	public String getDataDir() {
		return dataDir;
	}

	@JsonProperty
	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}
	
	@JsonProperty
	public String getLuceneDir() {
		return luceneDir;
	}

	@JsonProperty
	public void setLuceneDir(String luceneDir) {
		this.luceneDir = luceneDir;
	}
	
	@JsonProperty
	public String getXsdImportDir() {
		return xsdImportDir;
	}

	@JsonProperty
	public void setXsdImportDir(String xsdImportDir) {
		this.xsdImportDir = xsdImportDir;
	}

	@JsonProperty
	public String getXsdDownloadDir() {
		return xsdDownloadDir;
	}

	@JsonProperty
	public void setXsdDownloadDir(String xsdDownloadDir) {
		this.xsdDownloadDir = xsdDownloadDir;
	}

	@JsonProperty
	public String getVocabDownloadDir() {
		return vocabDownloadDir;
	}

	@JsonProperty
	public void setVocabDownloadDir(String vocabDownloadDir) {
		this.vocabDownloadDir = vocabDownloadDir;
	}
	

	@JsonProperty
	public String getVocabImportDir() {
		return vocabImportDir;
	}

	@JsonProperty
	public void setVocabImportDir(String vocabImportDir) {
		this.vocabImportDir = vocabImportDir;
	}
/*	
	@JsonProperty
	public String getServer() {
		return server;
	}

	@JsonProperty
	public void setServer(String server) {
		this.server = server;
	}
	
	@JsonProperty
	public String getRepo() {
		return this.repo;
	}

	@JsonProperty
	public void setRepo(String repo) {
		this.repo = repo;
	}	
*/	
	@JsonProperty
	public String getSitePrefix() {
		return sitePrefix;
	}

	@JsonProperty
	public void setSitePrefixDir(String sitePrefix) {
		this.sitePrefix = sitePrefix.endsWith("/") ? sitePrefix : sitePrefix + "/";
	}

	@JsonProperty
	public Map<String, Map<String, String>> getViews() {
		return views;
	}

	@JsonProperty
	public void setViews(Map<String, Map<String, String>> views) {
		final ImmutableMap.Builder<String, Map<String, String>> builder = ImmutableMap.builder();
		for (Map.Entry<String, Map<String, String>> entry : views.entrySet()) {
			builder.put(entry.getKey(), ImmutableMap.copyOf(entry.getValue()));
		}
		this.views = builder.build();
	}

	@Override
	public AssetsConfiguration getAssetsConfiguration() {
		return assets;
	}
	
}
