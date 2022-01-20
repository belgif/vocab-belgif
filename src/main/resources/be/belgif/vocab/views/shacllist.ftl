<#assign l = lang>
<!DOCTYPE html>
<html lang="${lang}">
<#include "head.ftl">
<body>
<#include "header.ftl">
<#assign m = messages>
<main class="container-fluid bg-light">
	<section>
		<h2>${m.getString("msg.shacls")}</h2>
		<h3>${m.getString("msg.overview")}</h3>
		<div class="table-responsive">
		<table class="table table-sm table-striped">
			<thead class="bg-dark text-light">
				<tr><th>${m.getString("msg.name")}</th>
					<th>${m.getString("msg.description")}</th>
					<th>${m.getString("msg.downloads")}</th>
				</tr>
			</thead>
			<tbody>
			<#list shacls?sort_by("id") as s>
				<#assign label = s.getLabel(l)!s.getLabel("")!"">
				<#assign comment = s.getComment(l)!s.getComment("")!"">
				<tr><td><a href="${s.id}">${label}</a></td>
					<td>${comment}</td>
				<#assign download = s.id?remove_ending("#")>
					<td><a href="${download}.ttl">TTL</a>&nbsp;
						<a href="${download}.jsonld">JSON-LD</a>&nbsp;
						<a href="${download}.nt">N-Triples</a></td>
				</tr>
			</#list>
			</tbody>
		</table>
		</div>
	</section>
</main>
<#include "footer.ftl">
</body>
</html>
