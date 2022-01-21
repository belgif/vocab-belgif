<#assign l = lang>
<!DOCTYPE html>
<html lang="${lang}">
<#include "head.ftl">
<body>
<#include "header.ftl">
<#assign m = messages>
<#assign o = onto>
<#assign c = o.classes>
<#assign p = o.properties> 
<main class="container-fluid bg-light">
	<section>
		<h1>${o.id.namespace}</h1>
		<div class="table-responsive">
		<table class="table table-sm table-striped table-hover table-bordered">
			<colgroup>
				<col class="col-sm-2">
				<col class="col-sm-10">
			</colgroup>
		<#assign val = o.getVersion(l)!o.getVersion("")!"">
		<#if val?has_content>
			<tr><th>${m.getString("msg.version")}</th><td>${val}</td></tr>
		</#if>
		<#assign val = o.getLabel(l)!"">
		<#if val?has_content>
			<tr><th>RDFS label</th><td>${val}</td></tr>
		</#if>
		<#assign val = o.getComment(l)!"">
		<#if val?has_content>
			<tr><th>RDFS comment</th><td>${val}</td></tr>
		</#if>
			<tr><th>Prefix</th><td>${o.id.namespace}</td></tr>
		</table>
		</div>
		<#assign clo = o.classesLetter>
		<#if clo?has_content>
		<section>
			<h2>Classes</h2>
			<div class="table-responsive">
			<table class="table table-sm table-striped table-hover table-bordered">
				<colgroup>
					<col class="col-sm-1">
					<col class="col-sm-11">
				</colgroup>
			<#list clo?keys as letter>
				<tr><th>${letter}</th><td>
					<#list clo[letter] as name>
						<a href="#${name}">${name}</a>
					</#list>
				</td></tr>
			</#list>
			</table>
			</div>
		</section>
		</#if>
		<#assign pro = o.propertiesLetter>
		<#if pro?has_content>
		<section>
			<h3>Properties</h3>
			<div class="table-responsive">
			<table class="table table-sm table-striped table-hover table-bordered">
				<colgroup>
					<col class="col-sm-1">
					<col class="col-sm-11">
				</colgroup>
			<#list pro?keys as letter>
				<tr><th>${letter}</th><td>
				<#list pro[letter] as name>
					<a href="#${name}">${name}</a>
				</#list>
				</td></tr>
			</#list>
			</table>
			</div>
		</section>
		</#if>
	</section>
	<#if c?has_content>
	<section>
		<h2>Classes</h2>
		<#list c?sort_by("id") as cl>
		<#assign name = cl.id.localName>
		<div class="table-responsive">
		<table id="${name}" class="table table-sm table-striped table-hover table-bordered">
			<colgroup>
				<col class="col-sm-2">
				<col class="col-sm-10">
			</colgroup>
			<thead class="bg-dark text-light">
				<tr><th colspan="2">${name}</th></tr>
			</thead>
			<tbody>
			<#list cl.subClasses as subc>
				<tr><td>SubClassOf</td><td><a href="${subc}">${subc}</a></td></tr>
			</#list>
			<#assign val = cl.getLabel(l)!cl.getLabel("")!"">
			<#if val?has_content>
				<tr><td>Label</td><td>${val}</td></tr>
			</#if>
			<#assign val = cl.getComment(l)!cl.getLabel("")!"">
			<#if val?has_content>
				<tr><td>Comment</td><td>${val}</td></tr>
			</#if>
			</tbody>
		</table>
		</div>
		</#list>
	</section>
	</#if>
	<#if p?has_content>
	<section>
		<h2>Properties</h2>
		<#list p?sort_by("id") as pr>
		<#assign name = pr.id.localName>
		<div class="table-responsive">
		<table id="${name}" class="table table-sm table-striped table-hover table-bordered">
			<colgroup>
				<col class="col-sm-2">
				<col class="col-sm-10">
			</colgroup>
			<thead class="bg-dark text-light">
				<tr><th colspan="2">${name}</th></tr>
			</thead>
			<tbody>
			<#list pr.subProperties as subp>
				<tr><td>SubPropertyOf</td><td><a href="${subp}">${subp}</a></td></tr>
			</#list>
			<#assign val = pr.getLabel(l)!pr.getLabel("")!"">
			<#if val?has_content>
				<tr><td>Label</td><td>${val}</td></tr>
			</#if>
			<#assign val = pr.getComment(l)!pr.getComment("")!"">
			<#if val?has_content>
				<tr><td>Comment</td><td>${val}</td></tr>
			</#if>
			<#assign vals = pr.domains>
			<#if vals?has_content>
				<tr><td>Domain</td><td>
				<#list vals as val>
					<a href="${val}">${val}</a>
				</#list>
				</td></tr>
			</#if>
			<#assign vals = pr.ranges>
			<#if vals?has_content>
				<tr><td>Range</td><td>
				<#list vals as val>
					<a href="${val}">${val}</a>
				</#list></td></tr>
			</#if>
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
