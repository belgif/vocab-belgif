<#assign l = lang>
<!DOCTYPE html>
<html lang="${lang}">
<#include "head.ftl">
<body>
<#include "header.ftl">
<#assign m = messages>
<#assign v = term>
<#assign langs = ['nl', 'fr', 'en', 'de']>
<main class="container-fluid bg-light">
	<section typeof="skos:Concept" about="${v.id}">
		<h1>${v.id}</h1>
		<section>
			<h2>${m.getString("msg.search")}</h2>
			<form method="get" action="/_search/${vocabName}">
				<input name="q" type="search"/>
				<input name="search" type="submit"/>
				<input name="lang" type="hidden" value="${l}"/>
			</form>
		</section>
		<section>
		<h2>${m.getString("msg.overview")}</h2>
		<div class="table-responsive">
		<table class="table table-sm table-striped table-hover table-bordered">
			<#assign n = v.notation!"">
			<#if n?has_content>
			<colgroup>
				<col class="col-sm-3">
				<col class="col-sm-9">
			</colgroup>
			<thead class="bg-dark text-light">
				<tr><th>${m.getString("msg.name")}</th>
					<th>${m.getString("msg.description")}</th>
				</tr>
			</thead>
			<tbody>
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
			<#list langs as lang>
				<#assign val = v.getDescription(lang)!"">
				<#if val?has_content>
					<tr><td property="dcterms:description" xml:lang="${lang}" content="${val}">Description (${lang})</td><td>${val}</td></tr>
				</#if>
			</#list>
			<#assign sd = v.startDate!"">
			<#assign ed = v.endDate!"">
			<#if sd?has_content || ed?has_content>
			<#setting datetime_format = "iso">
			<#if sd?has_content>
				<tr><td>Start Date</th>
					<td property="schema:startDate" content="${sd}">${sd}</td></tr>
			</#if>
			<#if ed?has_content>
				<tr><td>End Date</td>
					<td property="schema:endDate" content="${ed}">${ed}</td></tr>
			</#if>
			</#if>
			</tbody>
			</table>
			</div>
		</section>
		<section>
			<h2>Matches</h2>
			<div class="table-responsive">
			<table class="table table-sm table-striped table-hover table-bordered">
			<colgroup>
				<col class="col-sm-3">
				<col class="col-sm-9">
			</colgroup>
			<thead class="bg-dark text-light">
				<tr><th>${m.getString("msg.property")}</th>
					<th>${m.getString("msg.term")}</th>
				</tr>
			</thead>
			<tbody>
			<#list v.sameAs as val>
				<#assign link = val>
				<#if val?starts_with("http://data.europa")>
					<#assign link = "https://op.europa.eu/en/web/eu-vocabularies/concept/-/resource?uri=" + val>
				</#if>
				<tr><td>OWL sameAs</td>
					<td rel="owl:sameAs" resource="${val}"><a href="${link}">${val}</a></td></tr>
			</#list>
			<#list v.exactMatches as val>
				<#assign link = val>
				<#if val?starts_with("http://data.europa")>
					<#assign link = "https://op.europa.eu/en/web/eu-vocabularies/concept/-/resource?uri=" + val>
				</#if>
				<tr><td>Exact</td>
					<td rel="skos:exactMatch" resource="${val}"><a href="${link}">${val}</a></td></tr>
			</#list>
			<#list v.closeMatches as val>
				<#assign link = val>
				<#if val?starts_with("http://data.europa")>
					<#assign link = "https://op.europa.eu/en/web/eu-vocabularies/concept/-/resource?uri=" + val>
				</#if>
				<tr><td>Close</td>
					<td rel="skos:closeMatch" resource="${val}"><a href="${link}">${val}</a></td></tr>
			</#list>
			<#list v.broadMatches as val>
				<#assign link = val>
				<#if val?starts_with("http://data.europa")>
					<#assign link = "https://op.europa.eu/en/web/eu-vocabularies/concept/-/resource?uri=" + val>
				</#if>
				<tr><td>Broader</td>
					<td rel="skos:broadMatch" resource="${val}"><a href="${link}">${val}</a></td></tr>
			</#list>
			<#list v.narrowMatches as val>
				<#assign link = val>
				<#if val?starts_with("http://data.europa")>
					<#assign link = "https://op.europa.eu/en/web/eu-vocabularies/concept/-/resource?uri=" + val>
				</#if>
				<tr><td>Narrower</td>
					<td rel="skos:exactMatch" resource="${val}"><a href="${link}">${val}</a></td></tr>
			</#list>
			</body>
			</table>
			</div>
		</section>
		<#assign rels = v.broaders>
		<#if rels?has_content>
		<section>
			<h2>Broader</h2>
			<div class="table-responsive">
			<table class="table table-sm table-striped table-hover table-bordered">
			<thead class="bg-dark text-light">
				<tr><th>${m.getString("msg.term")}</th></tr>
			</thead>
			<tbody>
			<#list rels as rel>
				<tr><td rel="skos:broader" resource="${rel}"><a href="${rel}?lang=${l}">${rel}</a></td></tr>
			</#list>
			</tbody>
			</table>
			</div>
		</section>
		</#if>
		<#assign rels = v.narrowers>
		<#if rels?has_content>
		<section>
			<h2>Narrower</h2>
			<div class="table-responsive">
			<table class="table table-sm table-striped table-hover table-bordered">
			<thead class="bg-dark text-light">
				<tr><th>${m.getString("msg.term")}</th></tr>
			</thead>
			<tbody>
			<#list rels as rel>
				<tr><td rel="skos:narrower" resource="${rel}"><a href="${rel}?lang=${l}">${rel}</a></td></tr>
			</#list>
			</tbody>
			</table>
			</div>
		</section>
		</#if>
	</section>
</main>
<#include "footer.ftl">
</body>
</html>