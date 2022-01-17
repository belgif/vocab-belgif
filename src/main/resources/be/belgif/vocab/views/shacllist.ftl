<#assign l = lang>
<!DOCTYPE html>
<html lang="${lang}">
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
		<#if shacls?has_content>
		<section>
			<h3>SHACLs</h3>
			<section>
				<h4>${m.getString("msg.overview")}</h4>
				<table>
					<tr><th>${m.getString("msg.name")}</th>
						<th>${m.getString("msg.description")}</th>
						<th>${m.getString("msg.downloads")}</th>
					</tr>
				<#list shacls?sort_by("id") as s>
					<#assign label = s.getLabel(l)!s.getLabel("")!"">
					<#assign comment = s.getComment(l)!s.getComment("")!"">
					<tr><td><a href="${s.id}">${label}</a></td>
						<td>${comment}</td>
					<#assign download = s.id?remove_ending("#")>
						<td><a href="${download}.ttl">TTL</a>
							<a href="${download}.jsonld">JSON-LD</a>
							<a href="${download}.nt">N-Triples</a></td>
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
