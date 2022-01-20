<#assign l = lang>
<!DOCTYPE html>
<html lang="${lang}">
<#include "head.ftl">
<body>
<#include "header.ftl">
<#assign m = messages>
<main class="container-fluid bg-light">
	<section>
		<h2>${m.getString("msg.ctxs")}</h2>
		<h3>${m.getString("msg.overview")}</h3>
		<div class="table-responsive">
		<table class="table table-sm table-striped">
			<thead>
				<tr><th>${m.getString("msg.name")}</th>
					<th>${m.getString("msg.description")}</th>
					<th>${m.getString("msg.downloads")}</th>
				</tr>
			</thead>
			<tbody>
			<#list contexts as c>
				<tr><td>${c.getTitle(l)!""}</td>
					<td>${c.getDescription(l)!""}</td>
					<td><a href="${c.download!""}">JSON-LD</a></td>
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
