<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8'>
<link rel="stylesheet" type="text/css" href="/static/style.css" />
<link rel="alternate" type="text/turtle" href="/ns" />
<#include "title.ftl">
</head>
<body>
<#include "header.ftl">
<#assign l = lang>
<#assign m = messages>
<main>
    <div id="container">
    <#include "message.ftl">
    <#if ontos?has_content>
    <section>
	<h3>Ontologies</h3>
	<section>
	    <h4>${m.getString("msg.overview")}</h4>
	    <table>
	    <tr><th>${m.getString("msg.name")}</th>
		<th>${m.getString("msg.description")}</th>
		<th>${m.getString("msg.downloads")}</th>
	    </tr>
	    <#list ontos as o>
	    <tr><td><a href="${o.id}">${o.literal("rdfs", "label", l)!""}</a></td>
		<td>${o.literal("rdfs", "comment", l)!""}</td>
                <#assign download = o.download?remove_ending("#")>
                <td><a href="${download}.ttl">TTL</a>
                    <a href="${download}.jsonld">JSON-LD</a>
                    <a href="${download}.nt">N-Triples</a></td>
	    </tr>
	    </#list>
	    </table>
	</section>
    </section>
    </#if>
    <#if xmlns?has_content>
    <section>
	<h3>XML Namespaces</h3>
	<section>
	    <h4>${m.getString("msg.overview")}</h4>
	    <table>
	    <tr><th>${m.getString("msg.name")}</th>
		<th>${m.getString("msg.description")}</th>
		<th>${m.getString("msg.downloads")}</th>
	    </tr>
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
    </div>
</main>
<#include "footer.ftl">
</body>
</html>
