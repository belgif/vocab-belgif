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
package be.belgif.vocab;

import java.io.IOException;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Bart.Hanssens
 */
@PreMatching
@Priority(200)
public class ContentNegFilter implements ContainerRequestFilter{
	private final Logger LOG = (Logger) LoggerFactory.getLogger(ContentNegFilter.class);
	
/*	
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) 
						throws IOException, ServletException {
		System.err.println("In Filter");
		LOG.warn("infilter");
		if (req instanceof HttpServletRequest) {
			HttpServletRequest httpReq = (HttpServletRequest) req;
			String uri = httpReq.getRequestURI();
			if (uri.matches("(?!.*xsd)/ns/.*")) {
				LOG.warn("found");
			
				String ctype = httpReq.getHeader(HttpHeaders.ACCEPT);
			//	if (ctype.equals(MediaType.TEXT_HTML)) {
					RequestDispatcher dispatcher = 
						req.getRequestDispatcher(uri + "." + "html");
					if (dispatcher == null) {
						LOG.warn("no dispatcher");
					}
					dispatcher.forward(req, resp);
			//	}
			}
		}
	}

*/	
	@Override
	public void filter(ContainerRequestContext rc) throws IOException {
		LOG.warn("in filter");
		UriInfo uriInfo = rc.getUriInfo();
		String path = uriInfo.getRequestUri().getRawPath();
		
				LOG.warn("in filter " + path);
		if (!path.startsWith("/ns")) {
			return;
		}
		if (path.endsWith("xsd")) {
			return;
		}
		LOG.warn(path + ".html");
		
	System.err.println(path + ".html");
		rc.setRequestUri(uriInfo.getRequestUriBuilder().replacePath(path + ".html").build());
	}
	
	 public ContentNegFilter() {
		LOG.warn("initialize");
	 }
}
