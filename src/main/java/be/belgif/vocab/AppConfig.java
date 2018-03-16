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

import io.dropwizard.Configuration;
//import io.dropwizard.bundles.assets.AssetsBundleConfiguration;
//import io.dropwizard.bundles.assets.AssetsConfiguration;

import java.util.Map;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Bart.Hanssens
 */
public class AppConfig extends Configuration { // implements AssetsBundleConfiguration {
	public class ImportDownloadFactory {
		@NotEmpty
		private String importDir;

		@NotEmpty
		private String downloadDir;

		
		@JsonProperty
		public String getDownloadDir() {
			return downloadDir;
		}

		@JsonProperty
		public void setDownloadDir(String downloadDir) {
			this.downloadDir = downloadDir;
		}
		
		@JsonProperty
		public String getImportDir() {
			return importDir;
		}

		@JsonProperty
		public void setImportDir(String importDir) {
			this.importDir = importDir;
		}
	}

	@NotEmpty
	private String dataDir;
	
	@NotEmpty
	private String luceneDir;

	@NotEmpty
	private String sitePrefix;

	@NotEmpty
	private Map<String, Map<String, String>> views;

	@NotNull
	@JsonProperty("vocabularies")
	private ImportDownloadFactory vocabs = new ImportDownloadFactory();

	@NotNull
	@JsonProperty("xmlnamespaces")
	private ImportDownloadFactory xsds = new ImportDownloadFactory();
	
	@NotNull
	@JsonProperty("ontologies")
	private ImportDownloadFactory ontos = new ImportDownloadFactory();
	
/*	@Valid
	@NotNull
	@JsonProperty
	private final AssetsConfiguration assets = AssetsConfiguration.builder().build();
*/
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
		this.views = views;
	}

	@JsonProperty
	public ImportDownloadFactory getOntos() {
		return ontos;
	}
	
	@JsonProperty
	public void setOntos(ImportDownloadFactory ontos) {
		this.ontos = ontos;
	}
	
	@JsonProperty
	public ImportDownloadFactory getVocabs() {
		return vocabs;
	}
	
	@JsonProperty
	public void setVocabs(ImportDownloadFactory vocabs) {
		this.vocabs = vocabs;
	}

	@JsonProperty
	public ImportDownloadFactory getXsds() {
		return xsds;
	}
	
	@JsonProperty
	public void setXsds(ImportDownloadFactory xsds) {
		this.xsds = xsds;
	}

/*	@Override
	public AssetsConfiguration getAssetsConfiguration() {
		return assets;
	} */
}
