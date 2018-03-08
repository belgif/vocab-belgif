<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8'>
<link rel="stylesheet" type="text/css" href="/static/style.css" />
<link rel="alternate" type="text/turtle" href="/void" />
<title>Vocab Belgif - DEMO</title>
</head>
<body>
<#include "header.ftl">
<#assign m = messages>
<main>
    <div id="container">
    <#include "message.ftl">
    <#if onto??>
    <section>
	<h3>Ontologies</h3>
	<section>
	    <h4>${m.getString("msg.overview")}</h4>
	    <table>
	    <tr><th>${m.getString("msg.name")}</th>
		<th>${m.getString("msg.description")}</th>
		<th>${m.getString("msg.downloads")}</th>
	    </tr>
	    <#assign l = lang>
	    <#list onto as o>
	    <tr><td><a href="${o.root}">${v.literal("dcterms", "title", l)!""}</a></td>
		<td>${o.literal("dcterms", "description", l)!""}</td>
		<td><a href="${o.shacl!""}">SHACL</a></td>
	    </tr>
	    </#list>
	    </table>
	</section>
    </section>
    </#if>
    <#if xmlns??>
    <section>
	<h3>Namespaces</h3>
	<section>
	    <h4>${m.getString("msg.overview")}</h4>
	    <table>
	    <tr><th>${m.getString("msg.name")}</th>
		<th>${m.getString("msg.description")}</th>
		<th>${m.getString("msg.downloads")}</th>
	    </tr>
	    <#assign l = lang>
	    <#list xmlns as n>
	    <tr><td>${n.literal("dcterms", "title", l)!""}</td>
		<td>${n.literal("dcterms", "description", l)!""}</td>
		<td><a href="${n.download!""}">XSD</a></td>
	    </tr>
	    </#list>
	    </table>
	</section>
    </section>
    </#if>
    <#if vocabs??>
    <section>
	<h3>Thesauri</h3>
	<section>
	    <h4>${m.getString("msg.overview")}</h4>
	    <table>
	    <tr><th>${m.getString("msg.name")}</th>
		<th>${m.getString("msg.description")}</th>
		<th>${m.getString("msg.downloads")}</th>
	    </tr>
	    <#assign l = lang>
	    <#list vocabs?sort_by("root") as v>
	    <tr><td><a href="${v.root}">${v.literal("dcterms", "title", l)!""}</a></td>
		<td>${v.literal("dcterms", "description", l)!""}</td>
		<td><a href="${v.download!""}.ttl">TTL</a>
		    <a href="${v.download!""}.jsonld">JSON-LD</a>
		    <a href="${v.download!""}.nt">N-Triples</a></td>
	    </tr>
	    </#list>
	    </table>
	</section>
	<section>
	    <h4>Content Negotiation (De-referencing)</h4>
	    <table>
	    <tr><th>${m.getString("msg.format")}</th><th>HTTP Accept:</th></tr>
	    <tr><td>HTML</td><td>text/html</td></tr>
	    <tr><td>JSON-LD</td><td>application/ld+json</td></tr>
	    <tr><td>N-Triples</td><td>application/n-triples</td></tr>
	    <tr><td>Turtle</td><td>text/turtle</td></tr>
	    </table>
	</section>
	<section>
	    <h4>Linked Data Fragments</h4>
	    <p>http://vocab.belgif.be/_ldf</p>
	</section>
    </section>
    </#if>
    </div>
</main>
<#include "footer.ftl">
</body>
</html>
