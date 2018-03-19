<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8'>
<link rel="stylesheet" type="text/css" href="/static/style.css" />
<link rel="alternate" type="text/turtle" href="/shacl" />
<#include "title.ftl">
</head>
<body>
<#include "header.ftl">
<#assign l = lang>
<#assign m = messages>
<main>
    <div id="container">
    <#include "message.ftl">
    <#if shacls?has_content>
    <section>
	<h3>SHACLs</h3>
	<section>
	    <h4>${m.getString("msg.overview")}</h4>
	    <table>
	    <tr><th>${m.getString("msg.name")}</th>
		<th>${m.getString("msg.description")}</th>
		<th>${m.getString("msg.downloads")}</th>
	    </tr>
	    <#list shacls as s>
	    <tr><td><a href="${s.id}">${s.literal("rdfs", "label", l)!""}</a></td>
		<td>${s.literal("rdfs", "comment", l)!""}</td>
                <#assign download = s.download?remove_ending("#")>
                <td><a href="${download}.ttl">TTL</a>
                    <a href="${download}.jsonld">JSON-LD</a>
                    <a href="${download}.nt">N-Triples</a></td>
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
