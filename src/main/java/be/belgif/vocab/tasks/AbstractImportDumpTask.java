/*
 * Copyright (c) 2018, Bart Hanssens <bart.hanssens@bosa.fgov.be>
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

import be.belgif.vocab.helpers.QueryHelper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.BasicWriterSettings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Import a RDF file and create full (static) download files in various formats.
 *
 * @author Bart.Hanssens
 */
public abstract class AbstractImportDumpTask extends AbstractImportTask {
	protected final String[] ftypes = 
		new String[] { RDFFormat.TURTLE.getDefaultFileExtension(), 
				RDFFormat.JSONLD.getDefaultFileExtension(), 
				RDFFormat.NTRIPLES.getDefaultFileExtension() };
	protected final String downloadDir;
	
	private final Logger LOG = (Logger) LoggerFactory.getLogger(AbstractImportDumpTask.class);
	
	
	/**
	 * Export the triples to data dump files of various formats
	 *
	 * @param conn triple store repository connection
	 * @param name name of the thesaurus
	 * @param ctx context / named graph
	 * @throws IOException
	 */
	protected void writeDumps(RepositoryConnection conn, String name, Resource ctx) 
									throws IOException {
		LOG.info("Writing data dumps for {} from {}", name, ctx);
		
		conn.clearNamespaces();
		QueryHelper.NS_MAP.forEach((p, n) -> conn.setNamespace(p, n));
				
		for (String ftype : ftypes) {
			Path f = Paths.get(downloadDir, name + "." + ftype);
			try (BufferedWriter w = Files.newBufferedWriter(f, StandardOpenOption.WRITE,
					StandardOpenOption.CREATE)) {
				Optional<RDFFormat> fmt
						= Rio.getWriterFormatForFileName(name + "." + ftype);
				if (fmt.isPresent()) {
					RDFWriter rdfh = Rio.createWriter(fmt.get(), w);
					rdfh.set(BasicWriterSettings.PRETTY_PRINT, true);
					rdfh.set(BasicWriterSettings.INLINE_BLANK_NODES, true);
					LOG.info("Writing file {}", f);
					conn.export(rdfh, ctx);
				} else {
					throw new IOException("No RDF writer found for " + ftype);
				}
			}
		}
	}
	
	/**
	 * Constructor
	 *
	 * @param task task name
	 * @param repo triple store
	 * @param inDir import directory
	 * @param outDir download directory
	 * @param type type of file to import
	 */
	public AbstractImportDumpTask(String task, Repository repo, 
					String inDir, String outDir, String type) {
		super(task, repo, inDir, type);
		this.downloadDir = outDir;
	}
}
