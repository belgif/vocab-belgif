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
package be.belgif.vocab.tasks;

import io.dropwizard.servlets.tasks.Task;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.ws.rs.WebApplicationException;

import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.Sail;
import org.eclipse.rdf4j.sail.lucene.LuceneSail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Re-index Lucene
 *
 * @author Bart.Hanssens
 */
public class LuceneReindexTask extends Task {
	public final static String NAME = "lucene-reindex";

	private final Repository repo;

	private final static Logger LOG = (Logger) LoggerFactory.getLogger(LuceneReindexTask.class);

	/**
	 * Execute task
	 *
	 * @param param parameters
	 * @param w output writer
	 * @throws Exception
	 */
	@Override
	public void execute(Map<String,List<String>> param, PrintWriter w) throws Exception {
		if (repo instanceof SailRepository) {
			Sail sail = ((SailRepository) repo).getSail();
			if (sail instanceof LuceneSail) {
				LOG.info("Reindexing lucene sail");
				((LuceneSail) sail).reindex();
				LOG.info("Done");
			}
		} else {
			throw new WebApplicationException("Not a Sail repository");
		}
	}

	/**
	 * Constructor
	 *
	 * @param repo triple store
	 */
	public LuceneReindexTask(Repository repo) {
		super(NAME);
		this.repo = repo;
	}

}