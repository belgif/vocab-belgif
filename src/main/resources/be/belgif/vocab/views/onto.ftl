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
<#assign langs = ['nl', 'fr', 'en', 'de', '']>
<#assign m = messages>
<#assign o = onto>
<#assign c = o.classes>
<#assign p = o.properties> 
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
	<#if c?has_content>
	<section>
	    <h4>Classes</h4>
	    <table>
	    <#list c?sort_by("id") as cl>
		<tr><td><a href="${cl.id}">${cl.id}</a></td></tr>
	    </#list>
	    </table>
	</section>
	</#if>
	<#if p?has_content>
	<section>
	    <h4>Properties</h4>
	    <table>
	    <#list p?sort_by("id") as pr>
		<tr><td><a href="${pr.id}">${pr.id}</a></td></tr>
	    </#list>
	    </table>
	</section>
	</#if>
    </section>
    <#if c?has_content>
    <section>
	<h3>Classes</h3>
	<#list c?sort_by("id") as cl>
            <a name="${cl.id}">
            <table>
                <tr><th colspan="2">${cl.id}</th></tr>
                <#assign val = cl.objs("rdfs", "subClassOf")>
                <#if val?has_content>
                    <#list val as subc>
                        <tr><td>SubClassOf</td><td><a href="${subc}">${subc}</a></td></tr>
                    </#list>
		</#if>
                <#assign val = cl.lit("rdfs:label", lang)!"">
		<#if val?has_content>
		    <tr><td>Label (${lang})</td><td>${val}</td></tr>
                <#else>
                    <#assign val = cl.lit("rdfs:label", "")!"">
                    <#if val?has_content>
		    <tr><td>Label</td><td>${val}</td></tr>
                    </#if>
		</#if>
                <#assign val = cl.lit("rdfs:comment", lang)!"">
		<#if val?has_content>
		    <tr><td>Comment (${lang})</td><td>${val}</td></tr>
                <#else>
                    <#assign val = cl.lit("rdfs:label", "")!"">
                    <#if val?has_content>
		    <tr><td>RDFS Comment</td><td>${val}</td></tr>
                    </#if>
		</#if>
            </table>
	</#list>
    </section>
    </#if>
    <#if p?has_content>
    <section>
	<h3>Properties</h3>
	<#list p?sort_by("id") as pr>
            <a name="${pr.id}">
            <table>
                <tr><th colspan="2">${pr.id}</th></tr>
                <#assign val = pr.objs("rdfs", "subPropertyOf")>
                <#if val?has_content>
                    <#list val as subp>
                        <tr><td>SubPropertyOf</td><td><a href="${subp}">${subp}</a></td></tr>
                    </#list>
		</#if>
                <#assign val = pr.lit("rdfs:label", lang)!"">
		<#if val?has_content>
		    <tr><td>Label (${lang})</td><td>${val}</td></tr>
                <#else>
                    <#assign val = pr.lit("rdfs:label", "")!"">
                    <#if val?has_content>
		    <tr><td>Label</td><td>${val}</td></tr>
                    </#if>
		</#if>
                <#assign val = pr.lit("rdfs:comment", lang)!"">
		<#if val?has_content>
		    <tr><td>Comment (${lang})</td><td>${val}</td></tr>
                <#else>
                    <#assign val = pr.lit("rdfs:comment", "")!"">
                    <#if val?has_content>
		    <tr><td>Comment</td><td>${val}</td></tr>
                    </#if>
		</#if>
                <#assign val = pr.obj("rdfs", "domain")!"">
		<#if val?has_content>
		    <tr><td>Domain</td><td><a href="${val}">${val}</a></td></tr>
		</#if>
                <#assign val = pr.obj("rdfs", "range")!"">
		<#if val?has_content>
		    <tr><td>Range</td><td><a href="${val}">${val}</a></td></tr>
		</#if>
            </table>
	</#list>
    </section>
    </#if>
    </div>
</main>
<#include "footer.ftl">
</body>
</html>
