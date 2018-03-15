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
<#assign v = term>
<#assign langs = ['nl', 'fr', 'en', 'de']>
<main>
    <div id="container">
    <#include "message.ftl">
    <section>
	<h3>${v.id}</h3>
	<section>
	    <h4>${m.getString("msg.search")}</h4>
	    <form method="get" action="/_search/${vocab}">
		<input name="q" type="search"/>
		<input name="search" type="submit"/>
	    </form>
	</section>
	<section>
	    <h4>${m.getString("msg.general")}</h4>
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
    </div>
</main>
<#include "footer.ftl">
</body>
</html>