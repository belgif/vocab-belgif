<#assign l = lang>
<!DOCTYPE html>
<html lang="${lang}">
<#include "head.ftl">
<body>
<#include "header.ftl">
<#assign m = messages>
<#assign vs = vocabs>
<main class="container-fluid bg-light">
	<#if vs?has_content>
		<section>
		<h2>Thesauri</h2>
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
					<#list vs?sort_by("id") as v>
					<tr><td><a href="${v.id}">${v.getTitle(l)!""}</a></td>
						<td>${v.getDescription(l)!""}</td>
						<#assign download = v.download!"">
						<td><a href="${download}.ttl">TTL</a>
							<a href="${download}.jsonld">JSON-LD</a>
							(<a href="${download}-framed.jsonld">framed</a>)
							<a href="${download}.nt">N-Triples</a></td>
					</tr>
					</#list>
				</tbody>
			</table>
		</div>
		</section>
	</#if>
	<#include "contentneg.ftl">
</main>
<#include "footer.ftl">
</body>
</html>
