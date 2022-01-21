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
	<section typeof="skos:ConceptScheme" about="${v.id}">
		<h1>${v.id}</h1>
		<section>
			<h2>${m.getString("msg.search")}</h2>
			<form method="get" action="/_search/${vocabName}">
				<input name="q" type="search"/>
				<input name="search" type="submit"/>
			</form>
		</section>
		<section>
			<h2>${m.getString("msg.general")}</h2>
			<div class="table-responsive">
			<table class="table table-sm table-striped table-hover table-bordered">
				<colgroup>
					<col class="col-sm-2">
					<col class="col-sm-10">
				</colgroup>
				<#assign val = v.getVersion(lang)!v.getVersion("")!"">
				<#if val?has_content>
				<thead class="bg-dark text-light">
					<tr><td>${m.getString("msg.version")}</td><td>${val}</td></tr>
				</thead>
				</#if>
				<tbody>
				<#list langs as lang>
				<#assign val = v.getDescription(lang)!"">
				<#if val?has_content>
					<tr><td>DCTERMS description (${lang})</td>
						<td property="dcterms:description" xml:lang="${lang}" content="${val}">${val}</td></tr>
				</#if>
				</#list>
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
			<#list tc as t>
				<tr><td rel="skos:hasTopConcept" resource="${t}"><a href="${t}">${t}</a></td></tr>
			</#list>
			</table>
			</div>
		</section>
		</#if>
	</section>
</main>
<#include "footer.ftl">
</body>
</html>
