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
package be.belgif.vocab.tasks;

import be.belgif.vocab.App;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMultimap;

import io.dropwizard.servlets.tasks.Task;
import java.io.IOException;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import java.util.Optional;

import javax.ws.rs.WebApplicationException;
import org.eclipse.rdf4j.common.iteration.Iterations;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.vocabulary.DCTERMS;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.eclipse.rdf4j.model.vocabulary.VOID;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Import a SKOS file and create full (static) download files in various
 * 
 * @author Bart.Hanssens
 */
public class VocabImportTask extends Task {
	private final String importDir;
	private final String downloadDir;
	private final Repository repo;
	
	private final Logger LOG = (Logger) LoggerFactory.getLogger(VocabImportTask.class);
	
	
	private Model addVOID(RepositoryConnection conn, String name, Resource ctx) {
		Model m = new LinkedHashModel();
		ValueFactory f = conn.getValueFactory();
		IRI voidID  = f.createIRI("#" + name);
		
		m.add(voidID, RDF.TYPE, VOID.DATASET);
		m.add(voidID, DCTERMS.MODIFIED, f.createLiteral(new Date()));
		m.add(voidID, FOAF.HOMEPAGE, f.createIRI(App.PREFIX));
		m.add(voidID, VOID.DATASET, f.createIRI(App.PREFIX + "dataset/" + name));
		m.add(voidID, VOID.TRIPLES, f.createLiteral(conn.size(ctx)));
		m.add(voidID, VOID.VOCABULARY, f.createIRI(SKOS.NAMESPACE));
		m.add(voidID, VOID.URI_SPACE, f.createLiteral(App.PREFIX + name));
		
		return m;
	}
	
	/**
	 * Import triples from file into RDF store, using name as vocabulary name.
	 * 
	 * @param file input file path
	 * @param name short name
	 * @param format RDF format
	 */
	private void importFile(Path file, String name, RDFFormat format) {
		try (RepositoryConnection conn = repo.getConnection()) {
			String vocab = name.split("\\.")[0];
			Resource ctx = repo.getValueFactory().createIRI(App.PREFIX_GRAPH + vocab);
			conn.begin();
			conn.remove((Resource) null, null, null, ctx);
			conn.add(file.toFile(), null, format, ctx);
			conn.add(addVOID(conn, name, ctx), ctx);
			conn.commit();
		} catch (RepositoryException|IOException rex) {
			// will be rolled back automatically
			throw new WebApplicationException("Error adding statements", rex);
		}
	}
	
	/**
	 * Execute task
	 * 
	 * @param param parameters
	 * @param w output writer
	 * @throws Exception
	 */
	@Override
	@Timed
	public void execute(ImmutableMultimap<String, String> param, PrintWriter w) throws Exception {
		ImmutableCollection<String> names = param.get("name");
		if (names == null || names.isEmpty()) {
			throw new WebApplicationException("Param name empty");
		}
		
		String name = names.asList().get(0);
		Path infile = Paths.get(importDir, name);
		
		LOG.info("Trying to parse {}", infile);
		
		if (! Files.isReadable(infile)) {
			throw new WebApplicationException("File not readable");
		}	
		Optional<RDFFormat> format = Rio.getParserFormatForFileName(infile.toString());
		if (! format.isPresent()) {
			throw new WebApplicationException("File type not supported");
		}
		
		importFile(infile, name, format.get());

	}
	
	/**
	 * Constructor
	 * 
	 * @param repo triple store
	 * @param inDir import directory
	 * @param outDir download directory
	 */
	public VocabImportTask(Repository repo, String inDir, String outDir) {
		super("vocab-import");
		this.repo = repo;
		this.importDir = inDir;
		this.downloadDir = outDir;
	}
}
