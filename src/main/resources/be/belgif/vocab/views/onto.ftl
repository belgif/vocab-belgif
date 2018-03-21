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
        <tr><td>Prefix</td><td>${o.id.namespace}</td></tr>
        <#assign val = o.lit("owl:versionInfo", "")>
        <#if val?has_content>
            <tr><td>Version</td><td>${val}</td></tr>
        </#if>
        <#assign val = o.lit("rdfs:label", l)>
        <#if val?has_content>
            <tr><td>RDFS label</td><td>${val}</td></tr>
        </#if>
        <#assign val = o.lit("rdfs:comment", l)>
        <#if val?has_content>
            <tr><td>RDFS comment</td><td>${val}</td></tr>
        </#if>
	</table>
        <#assign clo = o.classesLetter>
	<#if clo?has_content>
	<section>
	    <h4>Classes</h4>
	    <table>
	    <#list clo?keys as letter>
		<tr><th>${letter}</th><td>
                    <#list clo[letter] as name>
                        <a href="#${name}">${name}</a>
                    </#list>
                </td></tr>
	    </#list>
	    </table>
	</section>
	</#if>
        <#assign pro = o.propertiesLetter>
	<#if pro?has_content>
	<section>
	    <h4>Properties</h4>
	    <table>
	    <#list pro?keys as letter>
                <tr><th>${letter}</th><td>
                    <#list pro[letter] as name>
                        <a href="#${name}">${name}</a>
                    </#list>
                </td></tr>
	    </#list>
	    </table>
	</section>
	</#if>
    </section>
    <#if c?has_content>
    <section>
        <h3>Classes</h3>
	<#list c?sort_by("id") as cl>
            <#assign name = cl.id.localName>
            <a name="${name}">
            <table class="onto">
                <tr><th colspan="2">${name}</th></tr>
                <#assign val = cl.objs("rdfs", "subClassOf")>
                <#if val?has_content>
                    <#list val as subc>
                        <tr><td>SubClassOf</td><td><a href="${subc}">${subc}</a></td></tr>
                    </#list>
		</#if>
                <#assign val = cl.lit("rdfs:label", lang)>
		<#if val?has_content>
		    <tr><td>Label (${lang})</td><td>${val}</td></tr>
                <#else>
                    <#assign val = cl.lit("rdfs:label", "")>
                    <#if val?has_content>
		    <tr><td>Label</td><td>${val}</td></tr>
                    </#if>
		</#if>
                <#assign val = cl.lit("rdfs:comment", lang)>
		<#if val?has_content>
		    <tr><td>Comment (${lang})</td><td>${val}</td></tr>
                <#else>
                    <#assign val = cl.lit("rdfs:label", "")>
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
            <#assign name = pr.id.localName>
            <a name="${name}">
            <table class="onto">
                <tr><th colspan="2">${name}</th></tr>
                <#assign val = pr.objs("rdfs", "subPropertyOf")>
                <#if val?has_content>
                    <#list val as subp>
                        <tr><td>SubPropertyOf</td><td><a href="${subp}">${subp}</a></td></tr>
                    </#list>
		</#if>
                <#assign val = pr.lit("rdfs:label", lang)>
		<#if val?has_content>
		    <tr><td>Label (${lang})</td><td>${val}</td></tr>
                <#else>
                    <#assign val = pr.lit("rdfs:label", "")>
                    <#if val?has_content>
		    <tr><td>Label</td><td>${val}</td></tr>
                    </#if>
		</#if>
                <#assign val = pr.lit("rdfs:comment", lang)>
		<#if val?has_content>
		    <tr><td>Comment (${lang})</td><td>${val}</td></tr>
                <#else>
                    <#assign val = pr.lit("rdfs:comment", "")>
                    <#if val?has_content>
		    <tr><td>Comment</td><td>${val}</td></tr>
                    </#if>
		</#if>
                <#assign vals = pr.objs("rdfs", "domain")>
		<#if vals?has_content>
		    <tr><td>Domain</td><td>
                    <#list vals as val>        
                        <a href="${val}">${val}</a>
                    </#list>
                    </td></tr>
		</#if>
                <#assign vals = pr.objs("rdfs", "range")>
		<#if vals?has_content>
		    <tr><td>Range</td><td>
                    <#list vals as val>        
                        <a href="${val}">${val}</a>
                    </#list></td></tr>
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
