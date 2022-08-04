<#assign l = lang>
<!DOCTYPE html>
<html lang="${lang}">
<#include "head.ftl">
<body>
<#include "header.ftl">
<#assign m = messages>
<#assign v = term>
<#assign lic = license>
<#assign rh = organization>
<#assign langs = ['nl', 'fr', 'en', 'de']>
<main class="container-fluid bg-light">
	<section typeof="skos:ConceptScheme" about="${v.id}">
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
				<colgroup>
					<col class="col-sm-3">
					<col class="col-sm-9">
				</colgroup>
				<thead class="bg-dark text-light">
					<tr><th>${m.getString("msg.property")}</th>
						<th>${m.getString("msg.description")}</th>
					</tr>
				</thead>
				<tbody>
				<#assign val = v.getVersion(lang)!v.getVersion("")!"">
				<#if val?has_content>
					<tr><td>${m.getString("msg.version")}</td><td>${val}</td></tr>
				</#if>
				<#assign val = v.getDescription(lang)!"">
				<#if val?has_content>
					<tr><td>${m.getString("msg.description")}</td>
						<td property="dcterms:description" xml:lang="${lang}" content="${val}">${val}</td></tr>
				</#if>
				<#if rh?has_content>
					<#assign val = rh.getLegalName(lang)!"">
					<tr><td>${m.getString("msg.rightsholder")}</td>
						<td property="dcterms:rightsHolder" resource="${rh.id}">${val}</td></tr>
				</#if>
				<#if lic?has_content>
					<#assign val = lic.getTitle(lang)!"">
					<tr><td>${m.getString("msg.license")}</td>
						<td property="dcterms:license" resource="${lic.id}">${val}</td></tr>
				</#if>
				</tbody>
			</table>
			</div>
		</section>
		<#assign tc = v.topConcepts>
		<#if tc?has_content>
		<section>
			<h2>SKOS TopConcepts</h2>
			<div class="table-responsive">
			<table class="table table-sm table-striped table-hover table-bordered">
			<thead class="bg-dark text-light">
				<tr><th>${m.getString("msg.term")}</th></tr>
			</thead>
			<tbody>
			<#list tc as t>
				<tr><td rel="skos:hasTopConcept" resource="${t}"><a href="${t}">${t}</a></td></tr>
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
