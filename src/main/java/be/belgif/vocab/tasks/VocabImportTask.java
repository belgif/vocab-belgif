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
import be.belgif.vocab.helpers.QueryHelper;
import be.belgif.vocab.ldf.QueryHelperLDF;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMultimap;

import io.dropwizard.servlets.tasks.Task;
import java.io.BufferedWriter;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.WebApplicationException;

import org.eclipse.rdf4j.common.iteration.Iterations;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
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
import org.eclipse.rdf4j.rio.RDFWriter;
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

	/**
	 * Export the triples to data dump files of various formats
	 * 
	 * @param conn triple store repository connection
	 * @param name name of the thesaurus
	 * @param ctx context / named graph
	 * @return 
	 */	
	private void writeDumps(RepositoryConnection conn, String name, Resource ctx) throws IOException {
		LOG.info("Writing data dumps for {}", name);
			
		for(String ftype: App.FTYPES.keySet()) {
			Path p = Paths.get(downloadDir, name + "." + ftype);
			try (BufferedWriter w = Files.newBufferedWriter(p, StandardOpenOption.WRITE, 
															StandardOpenOption.CREATE)) {
				Optional<RDFFormat> fmt  =
						Rio.getWriterFormatForFileName(name + "." + ftype);
				if (fmt.isPresent()) {
					RDFWriter rdfh = Rio.createWriter(fmt.get(), w);
					LOG.info("Writing file {}", p);
					conn.export(rdfh, ctx);
				} else {
					throw new IOException("No RDF writer found for " + ftype);
				}
			}
		}
	}
	
	/**
	 * Add VoID metadata about the thesaurus
	 * 
	 * @param conn triple store repository connection
	 * @param name name of the thesaurus
	 * @param ctx context / named graph
	 * @return VoID triples
	 */	
	private Model addVOID(RepositoryConnection conn, String name, Resource ctx) {
		LOG.info("Adding VOID metadata for {}", name);
		
		String prefix = App.getPrefix();
		
		Model m = new LinkedHashModel();
		ValueFactory f = conn.getValueFactory();
		IRI voidID  = QueryHelper.asDataset(name);

		m.add(voidID, RDF.TYPE, VOID.DATASET);

		// multi-lingual titles, descriptions etc
		List<IRI> props = Arrays.asList(DCTERMS.TITLE, DCTERMS.DESCRIPTION, 
										DCTERMS.LICENSE, DCTERMS.SOURCE);
		props.forEach( p -> 
			Iterations.asList(conn.getStatements(null, p, null, ctx)).forEach(
								s -> m.add(voidID, p, s.getObject()))
		);
		
		m.add(voidID, DCTERMS.MODIFIED, f.createLiteral(new Date()));
		m.add(voidID, FOAF.HOMEPAGE, f.createIRI(prefix));
		
		// information about downloadable file
		m.add(voidID, VOID.DATA_DUMP, f.createIRI(prefix + "dataset/" + name));
		m.add(voidID, VOID.FEATURE, f.createIRI("http://www.w3.org/ns/formats/N-Triples"));
		m.add(voidID, VOID.FEATURE, f.createIRI("http://www.w3.org/ns/formats/Turtle"));
		m.add(voidID, VOID.FEATURE, f.createIRI("http://www.w3.org/ns/formats/JSON-LD"));
		
		// linked data query service(s)
		m.add(voidID, VOID.URI_LOOKUP_ENDPOINT, f.createIRI(prefix + QueryHelperLDF.LDF + name));
		m.add(voidID, VOID.URI_LOOKUP_ENDPOINT, f.createIRI(prefix + QueryHelperLDF.LDF));
		
		// top level and examples
		Iterations.asList(conn.getStatements(null, RDF.TYPE, SKOS.CONCEPT_SCHEME, ctx)).forEach(
				s -> m.add(voidID, VOID.ROOT_RESOURCE, s.getSubject()));
		Iterations.asList(conn.getStatements(null, SKOS.TOP_CONCEPT_OF, null, ctx)).forEach(
				s -> m.add(voidID, VOID.EXAMPLE_RESOURCE, s.getSubject()));
		
		m.add(voidID, VOID.TRIPLES, f.createLiteral(conn.size(ctx)));
		m.add(voidID, VOID.URI_SPACE, f.createLiteral(prefix + name));
		m.add(voidID, VOID.VOCABULARY, f.createIRI(SKOS.NAMESPACE));

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
			Resource ctx = repo.getValueFactory().createIRI(App.getPrefixGraph() + vocab);
			
			conn.begin();
			
			conn.remove((Resource) null, null, null, ctx);
			conn.add(file.toFile(), null, format, ctx);
			Model voidM = addVOID(conn, vocab, ctx);
			conn.add(voidM, ctx);
			
			writeDumps(conn, vocab, ctx);
			
			conn.commit();
		} catch (RepositoryException|IOException rex) {
			// will be rolled back automatically
			throw new WebApplicationException("Error importing vocabulary", rex);
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
		
		LOG.info("Done");
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
