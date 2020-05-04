<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
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
			<a href="/">${m.getString("msg.home")}</a> / 
			<a href="/auth">${m.getString("msg.vocabs")}</a>
		</div>
		<#include "message.ftl">
		<section typeof="skos:ConceptScheme" about="${v.id}">
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
				<#assign val = v.getVersion(lang)!v.getVersion("")!"">
				<#if val?has_content>
					<tr><td>${m.getString("msg.version")}</td><td>${val}</td></tr>
				</#if>
				<#list langs as lang>
				<#assign val = v.getDescription(lang)!"">
				<#if val?has_content>
					<tr><td>DCTERMS description (${lang})</td>
						<td property="dcterms:description" xml:lang="${lang}" content="${val}">${val}</td></tr>
				</#if>
				</#list>
				</table>
			</section>
			<#assign tc = v.topConcepts>
			<#if tc?has_content>
			<section>
				<h4>SKOS TopConcepts</h4>
				<table>
				<#list tc as t>
					<tr><td rel="skos:hasTopConcept" resource="${t}"><a href="${t}">${t}</a></td></tr>
				</#list>
				</table>
			</section>
			</#if>
		</section>
	</section>
	</div>
</main>
<#include "footer.ftl">
</body>
</html>
