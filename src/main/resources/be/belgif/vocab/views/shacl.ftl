<#assign l = lang>
<!DOCTYPE html>
<html lang="${lang}">
<#include "head.ftl">
<body>
<#include "header.ftl">
<#assign m = messages>
<#assign sh = shacl>
<#assign s = sh.shapes>
<main>
	<div id="container">
	<section>
		<div id="breadcrumb">
			<a href="/">${m.getString("msg.home")}</a> / 
			<a href="/shacl">${m.getString("msg.shacls")}</a>
		</div>
		<#include "message.ftl">
		<section>
			<h3>${m.getString("msg.general")}</h3>
			<table>
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
		</section>
		<#if s?has_content>
		<section>
			<h3>Prefixes</h3>
			<table>
				<tr><th>Prefix</th><th>Namespace</th></tr>
				<#assign ns = sh.usedNamespaces>
				<#list ns?keys?sort as pref> 
					<tr><td>${pref}</td><td>${ns[pref]}</td></tr>
				</#list>
			</table>
		</section>
		<section>
			<h3>Node Shapes</h3>
			<#list s?sort_by("id") as ns>
			<table class="shacl">
				<#assign target = ns.target!"">
				<#if target?has_content>
					<tr><th colspan="4"><a href="${target}">${sh.getShort(target)}</a></th></tr>
				</#if>
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
			</table>
			</#list>
		</section>
		</#if>
	</section>
	</div>
</main>
<#include "footer.ftl">
</body>
</html>
