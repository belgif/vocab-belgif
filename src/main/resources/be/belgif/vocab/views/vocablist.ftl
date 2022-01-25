<#assign l = lang>
<!DOCTYPE html>
<html lang="${lang}">
<#include "head.ftl">
<body>
<#include "header.ftl">
<#assign m = messages>
<main class="container-fluid bg-light">
	<section>
		<h1>${m.getString("msg.vocabs")} - ${m.getString("msg.overview")}</h1>
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
			<#list vocabs?sort_by("id") as v>
				<tr><td><a href="${v.id}?lang=${l}">${v.getTitle(l)!""}</a></td>
					<td>${v.getDescription(l)!""}</td>
					<#assign download = v.download!"">
					<td><a href="${download}.ttl">TTL</a>&nbsp;
						<a href="${download}.jsonld">JSON-LD</a>&nbsp;
						(<a href="${download}-framed.jsonld">framed</a>)&nbsp;
						<a href="${download}.nt">N-Triples</a></td>
				</tr>
			</#list>
			</tbody>
		</table>
		</div>
	</section>
	<#include "contentneg.ftl">
</main>
<#include "footer.ftl">
</body>
</html>
