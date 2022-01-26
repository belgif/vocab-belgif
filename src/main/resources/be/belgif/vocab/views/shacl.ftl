<#assign l = lang>
<!DOCTYPE html>
<html lang="${lang}">
<#include "head.ftl">
<body>
<#include "header.ftl">
<#assign m = messages>
<#assign sh = shacl>
<#assign s = sh.shapes>
<main class="container-fluid bg-light">
	<section>
		<h1>${sh.getLabel(l)!sh.getLabel("")!""}</h1>
		<div class="table-responsive">
		<table class="table table-sm table-striped table-hover table-bordered">
			<colgroup>
				<col class="col-sm-2">
				<col class="col-sm-10">
			</colgroup>
			<thead class="bg-dark text-light">
				<tr><th>${m.getString("msg.property")}</th>
					<th>${m.getString("msg.description")}</th>
				</tr>
			</thead>
			<#assign val = sh.getVersion(l)!sh.getVersion("")!"">
			<#if val?has_content>
				<tr><td>${m.getString("msg.version")}</td><td>${val}</td></tr>
			</#if>
			<#assign val = sh.getLabel(l)!sh.getLabel("")!"">
			<#if val?has_content>
				<tr><td>RDFS label</td><td>${val}</td></tr>
			</#if>
			<#assign val = sh.getComment(l)!sh.getComment("")!"">
			<#if val?has_content>
				<tr><td>RDFS comment</td><td>${val}</td></tr>
			</#if>
		</table>
		</div>
	</section>
	<#if s?has_content>
	<section>
		<h2>Prefixes</h2>
		<div class="table-responsive">
		<table class="table table-sm table-striped table-hover table-bordered">
			<thead class="bg-dark text-light">
				<tr><th>Prefix</th><th>Namespace</th></tr>
			</thead>
			<tbody>
			<#assign ns = sh.usedNamespaces>
			<#list ns?keys?sort as pref> 
				<tr><td>${pref}</td><td>${ns[pref]}</td></tr>
			</#list>
			</tbody>
		</table>
		</div>
	</section>
	<section>
		<h2>Node Shapes</h2>
		<#list s?sort_by("id") as ns>
		<div class="table-responsive">
		<table class="table table-sm table-striped table-hover table-bordered">
			<colgroup>
				<col class="col-sm-3">
				<col class="col-sm-1">
				<col class="col-sm-3">
				<col class="col-sm-5">
			</colgroup>
			<#assign target = ns.target!"">
			<#if target?has_content>
			<thead class="bg-dark text-light">
				<tr><th colspan="4"><a href="${target}" class="text-light">${sh.getShort(target)}</a></th></tr>
				<tr><th>Path</th><th>#</th><th>Target</th><th>Comment</th></tr>
			</thead>
			</#if>
			<tbody>
			<#assign props = ns.propertyShapes>
			<#list props?sort_by("path") as prop>
				<#assign path = prop.path!"">
				<tr>
					<td><a href="${path}">${sh.getShort(path)}</a></td>
					<td>${prop.minCount!"0"} - ${prop.maxCount!"N"}</td>
					<#assign typ = prop.dataType!prop.shClass!"">
					<td><a href="${typ}">${sh.getShort(typ)}</a></td>
					<td>
						<#assign langin = prop.langIn>
						<#if langin?has_content>
						${m.getString("msg.language")}:
						<#list langin as ls>
							${ls}
						</#list>
						</#if>
						<#assign nested = prop.propertyShapes>
						<#if nested?has_content>
						<#assign voc = nested?first.hasValue>
						<a href="${voc}">${voc}</a>
						</#if>
					</td>
				</tr>
			</#list>
			</tbody>
		</table>
		</div>
		</#list>
	</section>
	</#if>
</main>
<#include "footer.ftl">
</body>
</html>
