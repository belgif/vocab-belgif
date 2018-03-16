<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8'>
<link rel="stylesheet" type="text/css" href="/static/style.css" />
<title>Vocab Belgif - DEMO</title>
</head>
<body>
<#include "header.ftl">
<#assign m = messages>
<#assign o = onto>
<#assign langs = ['nl', 'fr', 'en', 'de']>
<main>
    <div id="container">
    <#include "message.ftl">
    <section>
        <h3>${m.getString("msg.general")}</h3>
        <table>
        <#list langs as lang>
            <#assign val = v.literal("dcterms", "description", lang)!"">
            <#if val?has_content>
                <tr><td>DCTERMS description (${lang})</td><td>${val}</td></tr>
            </#if>
        </#list>
	</table>
	<#assign c = v.objs("rdfs", "Class")>
	<#if c?has_content>
	<section>
	    <h4>Classes</h4>
	    <table>
	    <#list c as cl>
		<tr><td><a href="${cl}">${cl}</a></td></tr>
	    </#list>
	    </table>
	</section>
	</#if>
        <#assign p = v.objs("rdf", "Property")>
	<#if p?has_content>
	<section>
	    <h4>Properties</h4>
	    <table>
	    <#list p as pr>
		<tr><td><a href="${pr}">${pr}</a></td></tr>
	    </#list>
	    </table>
	</section>
	</#if>
    </section>
    <section>
        <#assign c = v.objs("rdfs", "Class")>
	<#if c?has_content>
	<section>
	    <h4>Classes</h4>
	    <table>
	    <#list c as cl>
		<tr><td><a href="${cl}">${cl}</a></td></tr>
	    </#list>
	    </table>
	</section>
	</#if>
        <#assign p = v.objs("rdf", "Property")>
	<#if p?has_content>
	<section>
	    <h4>Properties</h4>
	    <table>
	    <#list p as pr>
		<tr><td><a href="${pr}">${pr}</a></td></tr>
	    </#list>
	    </table>
	</section>
	</#if>
    </section>
</main>
<#include "footer.ftl">
</body>
</html>
