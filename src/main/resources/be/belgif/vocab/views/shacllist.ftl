<#assign l = lang>
<!DOCTYPE html>
<html lang="${lang}">
<#include "head.ftl">
<body>
<#include "header.ftl">
<#assign m = messages>
<main class="container-fluid bg-light">
	<section>
		<h1>${m.getString("msg.shacls")} - ${m.getString("msg.overview")}</h1>
		<div class="table-responsive">
		<table class="table table-sm table-striped table-hover table-bordered">
			<colgroup>
				<col class="col-sm-2">
				<col class="col-sm-6">
				<col class="col-sm-4">
			</colgroup>
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
					<td><a href="${download}.ttl">TTL</a>
						<a href="${download}.jsonld">JSON-LD</a>
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
