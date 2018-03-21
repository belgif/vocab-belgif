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

import be.belgif.vocab.helpers.ManagedRepository;
import be.belgif.vocab.health.RdfStoreHealthCheck;
import be.belgif.vocab.helpers.FileMessageBodyWriter;
import be.belgif.vocab.helpers.RDFMessageBodyWriter;
import be.belgif.vocab.resources.DatasetResource;
import be.belgif.vocab.resources.LdfResource;
import be.belgif.vocab.resources.NsResource;
import be.belgif.vocab.resources.RootResource;
import be.belgif.vocab.resources.SearchResource;
import be.belgif.vocab.resources.ShaclResource;
import be.belgif.vocab.resources.VocabResource;
import be.belgif.vocab.resources.VoidResource;
import be.belgif.vocab.tasks.LuceneReindexTask;
import be.belgif.vocab.tasks.OntoImportTask;
import be.belgif.vocab.tasks.ShaclImportTask;
import be.belgif.vocab.tasks.XmlnsRegisterTask;
import be.belgif.vocab.tasks.VocabImportTask;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.lucene.LuceneSail;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main Dropwizard web application
 *
 * @author Bart.Hanssens
 */
public class App extends Application<AppConfig> {

	private static String PREFIX;
	private static String PREFIX_GRAPH;

	private final Logger LOG = (Logger) LoggerFactory.getLogger(App.class);

	/**
	 * Configure a triple store repository
	 *
	 * @param config configuration object
	 * @return repository
	 */
	private Repository configRepo(AppConfig config) {
		// native disk-based store
		File dataDir = new File(config.getDataDir());
		NativeStore store = new NativeStore(dataDir);

		// full text search
		LuceneSail fts = new LuceneSail();
		fts.setParameter(LuceneSail.LUCENE_DIR_KEY, config.getLuceneDir());
		fts.setBaseSail(store);

		return new SailRepository(fts);
	}

	/**
	 * Get domain / prefix as string
	 *
	 * @return prefix
	 */
	public static String getPrefix() {
		return App.PREFIX;
	}

	/**
	 * Get graph IRI as string
	 *
	 * @return prefix
	 */
	public static String getPrefixGraph() {
		return App.PREFIX_GRAPH;
	}

	@Override
	public String getName() {
		return "lod-vocab";
	}

	@Override
	public void initialize(Bootstrap<AppConfig> config) {
		config.addBundle(new AssetsBundle("/assets/belgif.png", "/favicon.ico", null, "index"));
		config.addBundle(new AssetsBundle("/assets", "/static", null, "index"));
		
		config.addBundle(new ViewBundle<AppConfig>() {
			@Override
			public Map<String, Map<String, String>> getViewConfiguration(AppConfig config) {
				return config.getViews();
			}
		});
	}

	@Override
	public void run(AppConfig config, Environment env) {
		PREFIX = config.getSitePrefix();
		PREFIX_GRAPH = PREFIX + "graph";

		Repository repo = configRepo(config);

		// Managed resource
		env.lifecycle().manage(new ManagedRepository(repo));


		// RDF Serialization formats
		env.jersey().register(new FileMessageBodyWriter());
		env.jersey().register(new RDFMessageBodyWriter());

		// Resources / "web pages"
		env.jersey().register(new RootResource(repo));
		env.jersey().register(new VoidResource(repo));
		
		env.jersey().register(new VocabResource(repo));
		env.jersey().register(new DatasetResource(
						config.getVocabs().getDownloadDir()));
		env.jersey().register(new NsResource(repo, 
						config.getOntos().getDownloadDir(),
						config.getXsds().getDownloadDir()));
		env.jersey().register(new ShaclResource(repo, 
						config.getShacls().getDownloadDir()));
		
		// Services
		env.jersey().register(new SearchResource(repo));
		env.jersey().register(new LdfResource(repo));

		// Tasks
		env.admin().addTask(new OntoImportTask(repo, 
						config.getOntos().getImportDir(),
						config.getOntos().getDownloadDir()));
		env.admin().addTask(new ShaclImportTask(repo, 
						config.getShacls().getImportDir(),
						config.getShacls().getDownloadDir()));
		env.admin().addTask(new VocabImportTask(repo, 
						config.getVocabs().getImportDir(), 
						config.getVocabs().getDownloadDir()));
		env.admin().addTask(new XmlnsRegisterTask(repo, 
						config.getXsds().getImportDir()));
		
		// Full text search
		env.admin().addTask(new LuceneReindexTask(repo));
		

		// Monitoring
		RdfStoreHealthCheck check = new RdfStoreHealthCheck(repo);
		env.healthChecks().register("triplestore", check);
		
		env.lifecycle().addServerLifecycleListener(server -> {
			if (repo.getConnection().isEmpty()) {
				importAll(config);
			}
		});
		
	}

	/**
	 * Import files from a directory
	 * 
	 * @param dir directory containing the files
	 * @param target webclient
	 * @throws IOException 
	 */
	private void importFiles(String dir, WebTarget target) throws IOException {
		Path p = Paths.get(dir);
		
		File d = p.toFile();
		if (! (d.exists() && d.isDirectory() && d.canRead())) {
			LOG.error("Cannot read import files from {}", dir);
			return;
		}

		Files.list(p).forEach(f -> {
				target.queryParam("file", f.getFileName())
					.request()
					.post(Entity.text(""));
		});
	}

	/**
	 * Register all XSD namespace files and import all thesauri files.
	 * 
	 * @param config
	 * @throws IOException 
	 */
	private void importAll(AppConfig config) {
		String localhost = "http://localhost:8081";
		try {
			Client cl = ClientBuilder.newClient();
			WebTarget target = cl.target(localhost);

			importFiles(config.getOntos().getImportDir(),
					target.path("tasks/" + OntoImportTask.NAME));

			importFiles(config.getShacls().getImportDir(),
					target.path("tasks/" + ShaclImportTask.NAME));
			
			importFiles(config.getXsds().getImportDir(),
					target.path("tasks/" + XmlnsRegisterTask.NAME));
					
			importFiles(config.getVocabs().getImportDir(),
					target.path("tasks/" + VocabImportTask.NAME));
		
			cl.target(localhost).path("tasks/" + LuceneReindexTask.NAME)
				.request().post(Entity.text(""));
		} catch (IOException ioe) {
			LOG.error("Could not import on startup", ioe);
		}
	}
	
	
	/**
	 * Main
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new App().run(args);
	}
}
