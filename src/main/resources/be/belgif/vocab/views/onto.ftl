<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8'>
<link rel="stylesheet" type="text/css" href="/static/style.css" />
<#include "title.ftl">
</head>
<body>
<#include "header.ftl">
<#assign l = lang>
<#assign m = messages>
<#assign o = onto>
<main>
    <div id="container">
    <#include "message.ftl">
    <section>
        <h3>${m.getString("msg.general")}</h3>
        <table>
        <#assign val = o.lit("rdfs:label", l)!"">
        <#if val?has_content>
            <tr><td>RDFS label</td><td>${val}</td></tr>
        </#if>
        <#assign val = o.lit("rdfs:comment", l)!"">
        <#if val?has_content>
            <tr><td>RDFS comment</td><td>${val}</td></tr>
        </#if>
	</table>
	<#assign c = o.classes>
	<#if c?has_content>
	<section>
	    <h4>Classes</h4>
	    <table>
	    <#list c as cl>
		<tr><td><a href="${cl.id}">${cl.id}</a></td></tr>
	    </#list>
	    </table>
	</section>
	</#if>
        <#assign p = o.properties>
	<#if p?has_content>
	<section>
	    <h4>Properties</h4>
	    <table>
	    <#list p as pr>
		<tr><td><a href="${pr.id}">${pr.id}</a></td></tr>
	    </#list>
	    </table>
	</section>
	</#if>
    </section>
</main>
<#include "footer.ftl">
</body>
</html>
