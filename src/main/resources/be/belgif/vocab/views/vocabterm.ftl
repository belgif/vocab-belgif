<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8'>
<link rel="stylesheet" type="text/css" href="/static/style.css" />
<#include "title.ftl">
</head>
<body>
<#include "header.ftl">
<#assign m = messages>
<#assign v = term>
<#assign langs = ['nl', 'fr', 'en', 'de']>
<main>
    <div id="container">
    <section>
    <div id="breadcrumb">
        <a href="/">Home</a> / 
        <a href="/auth">${m.getString("msg.vocabs")}</a> /
        <a href="/auth/${vocabName}">${vocabName}</a>
    </div>
    </section>
    <#include "message.ftl">
    <section>
	<h3>${v.id}</h3>
	<section>
	    <h4>${m.getString("msg.search")}</h4>
	    <form method="get" action="/_search/${vocabName}">
		<input name="q" type="search"/>
		<input name="search" type="submit"/>
	    </form>
	</section>
	<section>
	    <h4>${m.getString("msg.general")}</h4>
	    <table>
	    <#assign n = v.notation!"">
	    <#if n?has_content>
		<tr><td>Notation</td><td>${n}</td></tr>
	    </#if>
            <#list langs as lang>
                <#list v.getPrefLabels(lang) as val>
                    <tr><td>PrefLabel (${lang})</td><td>${val}</td></tr>
                </#list>
            </#list>
            <#list langs as lang>
                <#list v.getAltLabels(lang) as val>
                    <tr><td>AltLabel (${lang})</td><td>${val}</td></tr>
                </#list>
            </#list>
	    </table>
        </section>
        <section>
	    <h4>Matches</h4>
	    <table>
	    <#list v.sameAs as s>
		<tr><td>OWL sameAs</td><td><a href="${s}">${s}</a></td></tr>
	    </#list>
            <#list v.exactMatches as val>
                <tr><td>Exact</td><td><a href="${val}">${val}</a></td></tr>
            </#list>
            <#list v.closeMatches as val>
                <tr><td>Close</td><td><a href="${val}">${val}</a></td></tr>
            </#list>
            <#list v.broadMatches as val>
                <tr><td>Broader</td><td><a href="${val}">${val}</a></td></tr>
            </#list>
            <#list v.narrowMatches as val>
                <tr><td>Narrower</td><td><a href="${val}">${val}</a></td></tr>
            </#list>
	    </table>
	</section>
        <#assign rels = v.broaders>
        <#if rels?has_content>
        <section>
            <h4>Broader</h4>
            <table>
            <#list rels as rel>
                <tr><td><a href="${rel}">${rel}</a></td></tr>
            </#list>
            </table>
        </section>
        </#if>
        <#assign rels = v.narrowers>
        <#if rels?has_content>
        <section>
            <h4>Narrower</h4>
            <table>
            <#list rels as rel>
                <tr><td><a href="${rel}">${rel}</a></td></tr>
            </#list>
            </table>
        </section>
        </#if>
    </section>
    </div>
</main>
<#include "footer.ftl">
</body>
</html>