<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8'>
<link rel="stylesheet" type="text/css" href="/static/style.css" />
<title>Belgif - Vocabularies DEMO</title>
</head>
<body>
<#include "header.ftl">
<main>
    <#assign v = term>
    <#assign langs = ['nl', 'fr', 'en', 'de']>
    <section>
	<h3>${v.id}</h3>
	<section>
	    <h4>Search</h4>
	    <form method="get" action="/_search/${vocab}">
		<input name="q" type="search"/>
		<input name="search" type="submit"/>
	    </form>
	</section>
	<section>
	    <h4>General</h4>
	    <table>
	    <#assign n = v.notation!"">
	    <#if n?has_content>
		<tr><td>SKOS notation</td><td>${n}</td></tr>
	    </#if>
	    <#assign labels = [ "prefLabel", "altLabel" ]>
	    <#list labels as label>
		<#list langs as lang>
		    <#assign val = v.literal("skos", label, lang)!"">
		    <#if val?has_content>
			<tr><td>SKOS ${label} (${lang})</td><td>${val}</td></tr>
		    </#if>
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
	    <#assign refs = [ "exactMatch", "closeMatch", "broadMatch", "narrowMatch" ]>
	    <#list refs as ref>
		<#list v.objs("skos", ref)!"" as val>
		    <tr><td>SKOS ${ref}</td><td><a href="${val}">${val}</a></td></tr>
		</#list>
	    </#list>
	    </table>
	</section>
	<#list [ "narrower", "broader" ] as bn>
	    <#assign rels = v.objs("skos", bn)>
	    <#if rels?has_content>
	    <section>
		<h4>SKOS ${bn}</h4>
		<table>
		<#list rels as rel>
		    <tr><td><a href="${rel}">${rel}</a></td></tr>
		</#list>
		</table>
	    </section>
	    </#if>
	</#list>
    </section>
</main>
<#include "footer.ftl">
</body>
</html>
