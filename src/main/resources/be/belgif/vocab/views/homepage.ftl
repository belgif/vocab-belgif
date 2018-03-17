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
<#assign l = lang>
<#assign m = messages>
<main>
    <div id="container">
    <#include "message.ftl">
    <#if vocabs?has_content>
    <section>
	<h3>Thesauri</h3>
	<section>
	    <h4>${m.getString("msg.overview")}</h4>
	    <table>
	    <tr><th>${m.getString("msg.name")}</th>
		<th>${m.getString("msg.description")}</th>
		<th>${m.getString("msg.downloads")}</th>
	    </tr>
	    <#list vocabs?sort_by("root") as v>
	    <tr><td><a href="${v.root}">${v.literal("dcterms", "title", l)!""}</a></td>
		<td>${v.literal("dcterms", "description", l)!""}</td>
                <#assign download = v.download!"">
		<td><a href="${download}.ttl">TTL</a>
		    <a href="${download}.jsonld">JSON-LD</a>
		    <a href="${download}.nt">N-Triples</a></td>
	    </tr>
	    </#list>
	    </table>
	</section>
    </section>
    <section>
	<#include "contentneg.ftl">
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
