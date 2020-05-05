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
			<a href="/auth">${m.getString("msg.vocabs")}</a> /
			<a href="/auth/${vocabName}">${vocabName}</a>
		</div>
		<#include "message.ftl">
		<section typeof="skos:Concept" about="${v.id}">
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
						<tr><td>Notation</td>
							<td property="skos:notation" content="${n}">${n}</td></tr>
					</#if>
					<#list langs as lang>
						<#list v.getPrefLabels(lang) as val>
							<tr><td>PrefLabel (${lang})</td>
								<td property="skos:prefLabel" xml:lang="${lang}" content="${val}">${val}</td></tr>
						</#list>
					</#list>
					<#list langs as lang>
						<#list v.getAltLabels(lang) as val>
							<tr><td property="skos:altLabel" xml:lang="${lang}" content="${val}">AltLabel (${lang})</td><td>${val}</td></tr>
						</#list>
					</#list>
				</table>
			</section>
			<#assign sd = v.startDate!"">
			<#assign ed = v.endDate!"">
			<#if sd?has_content || ed?has_content>
			<#setting datetime_format = "iso">
			<section>
				<h4>${m.getString("msg.validity")}</h4>
				<table>
				<#if sd?has_content>
					<#assign sd = sd?datetime>
					<tr><td>StartDate</td>
						<td property="schema:startDate" content="${sd}">${sd?date}</td></tr>
				</#if>
				<#if ed?has_content>
					<#assign ed = ed?datetime>
					<tr><td>EndDate</td>
						<td property="schema:endDate" content="${ed}">${ed?date}</td></tr>
				</#if>
				</table>
			</section>
			</#if>
			<section>
				<h4>Matches</h4>
				<table>
					<#list v.sameAs as s>
						<tr><td>OWL sameAs</td>
							<td rel="owl:sameAs" resource="${s}"><a href="${s}">${s}</a></td></tr>
					</#list>
					<#list v.exactMatches as val>
						<tr><td>Exact</td>
							<td rel="skos:exactMatch" resource="${val}"><a href="${val}">${val}</a></td></tr>
					</#list>
					<#list v.closeMatches as val>
						<tr><td>Close</td>
							<td rel="skos:closeMatch" resource="${val}"><a href="${val}">${val}</a></td></tr>
					</#list>
					<#list v.broadMatches as val>
						<tr><td>Broader</td>
							<td rel="skos:broadMatch" resource="${val}"><a href="${val}">${val}</a></td></tr>
					</#list>
					<#list v.narrowMatches as val>
						<tr><td>Narrower</td>
							<td rem="rel="skos:exactMatch" resource="${val}"><a href="${val}">${val}</a></td></tr>
					</#list>
				</table>
			</section>
			<#assign rels = v.broaders>
			<#if rels?has_content>
			<section>
				<h4>Broader</h4>
				<table>
					<#list rels as rel>
						<tr><td rel="skos:broader" resource="${rel}"><a href="${rel}">${rel}</a></td></tr>
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
					<tr><td rel="skos:narrower" resource="${rel}"><a href="${rel}">${rel}</a></td></tr>
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