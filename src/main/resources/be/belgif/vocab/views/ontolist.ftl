<#assign l = lang>
<!DOCTYPE html>
<html lang="${l}">
<#include "head.ftl">
<body>
<#include "header.ftl">
<#assign m = messages>
<main>
	<div id="container">
	<section>
		<div id="breadcrumb">
			<a href="/">${m.getString("msg.home")}</a>
		</div>
		<#include "message.ftl">
		<#if ontos?has_content>
		<section>
			<h3>${m.getString("msg.ontos")}</h3>
			<section>
				<h4>${m.getString("msg.overview")}</h4>
				<table>
					<tr><th>${m.getString("msg.name")}</th>
						<th>${m.getString("msg.description")}</th>
						<th>${m.getString("msg.downloads")}</th>
					</tr>
				<#list ontos?sort_by("id") as o>
					<tr><td><a href="${o.id}">${o.getLabel(l)!""}</a></td>
						<td>${o.getComment(l)!""}</td>
						<#assign download = o.id?remove_ending("#")>
						<td><a href="${download}.ttl">TTL</a>
							<a href="${download}.jsonld">JSON-LD</a>
							<a href="${download}.nt">N-Triples</a></td>
					</tr>
				</#list>
				</table>
			</section>
		</section>
		</#if>
		<#if xmlns?has_content>
		<section>
			<h3>XML Namespaces</h3>
			<section>
				<h4>${m.getString("msg.overview")}</h4>
				<table>
					<tr><th>${m.getString("msg.name")}</th>
						<th>${m.getString("msg.description")}</th>
						<th>${m.getString("msg.downloads")}</th>
					</tr>
					<#list xmlns as n>
					<tr><td>${n.getTitle(l)!""}</td>
						<td>${n.getDescription(l)!""}</td>
						<td><a href="${n.download!""}">XSD</a></td>
					</tr>
					</#list>
				</table>
			</section>
		</section>
		</#if>
	</section>
	</div>
</main>
<#include "footer.ftl">
</body>
</html>
