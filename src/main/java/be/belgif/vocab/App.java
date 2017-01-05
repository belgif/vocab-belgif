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
package be.belgif.vocab;

import be.belgif.vocab.health.RdfStoreHealthCheck;
import be.belgif.vocab.helpers.RDFMessageBodyReader;
import be.belgif.vocab.helpers.RDFMessageBodyWriter;
import be.belgif.vocab.resources.VocabResource;
import be.belgif.vocab.tasks.LuceneReindexTask;
import be.belgif.vocab.tasks.VocabImportTask;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

import java.io.File;

import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.lucene.LuceneSail;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;


/**
 * Main Dropwizard web application
 * 
 * @author Bart.Hanssens
 */
public class App extends Application<AppConfig> {
	
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

	@Override
	public String getName() {
		return "lod-vocab";
	}
	
	@Override
    public void run(AppConfig config, Environment env) {
		Repository repo = configRepo(config);
		//repo.initialize();
	
		// Managed resource
		env.lifecycle().manage(new ManagedRepository(repo));
		
		// RDF Serialization formats
		env.jersey().register(new RDFMessageBodyWriter());
		env.jersey().register(new RDFMessageBodyReader());
		//env.jersey().register(new HTMLMessageBodyWriter());
		
		// Resources
		env.jersey().register(new VocabResource(repo));
		
		// Tasks
		env.admin().addTask(new VocabImportTask(repo, config.getImportDir()));
		env.admin().addTask(new LuceneReindexTask());
				
		// Monitoring
		RdfStoreHealthCheck check = new RdfStoreHealthCheck(repo);
		env.healthChecks().register("triplestore", check);
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
