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

import java.io.IOException;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Import an ontology file and create (static) download files in various formats.
 *
 * @author Bart.Hanssens
 */
public class ShaclImportTask extends AbstractImportDumpTask {
	
	
	private final Logger LOG = (Logger) LoggerFactory.getLogger(ShaclImportTask.class);


	@Override
	protected void process(RepositoryConnection conn, String name, Resource ctx) throws IOException {
		writeDumps(conn, name, ctx);
		
		//conn.remove((Resource) null, null, null, ctx);
	}
	
	/**
	 * Constructor
	 *
	 * @param repo triple store
	 * @param inDir import directory
	 * @param outDir download directory
	 */
	public ShaclImportTask(Repository repo, String inDir, String outDir) {
		super("import-shacl", repo, inDir, outDir);
	}

}
