<#assign l = lang>
<!DOCTYPE html>
<html lang="${lang}">
<#include "head.ftl">
<body>
<#include "header.ftl">
<#assign m = messages>
<#assign langs = ['nl', 'fr', 'en', 'de']>
<main class="container-fluid bg-light">
	<h1>${m.getString("msg.results")}</h1>
	<div class="table-responsive">
	<table class="table table-sm table-striped table-hover table-bordered">
	<thead class="bg-dark text-light">
		<tr><th>ID</th><th>Label</th></tr>
	</thead>
	<tbody>
	<#list results as r>
		<tr><td><a href="${r.id}?lang=${l}">${r.id}</a></td><td>
			<#list langs as lang>
				<#list r.getPrefLabels(lang) as val>
					${val}<br>
				</#list>
			</#list>
		</td></tr>
	</#list>
	</tbody>
	</table>
	</div>
</main>
<#include "footer.ftl">
</body>
</html>
