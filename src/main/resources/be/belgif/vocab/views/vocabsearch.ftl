<#assign l = lang>
<!DOCTYPE html>
<html lang="${lang}">
<#include "head.ftl">
<body>
<#include "header.ftl">
<#assign m = messages>
<#assign langs = ['nl', 'fr', 'en', 'de']>
<main>
	<div id="container">
	<section>
		<div id="breadcrumb">
			<a href="/">${m.getString("msg.home")}</a> / 
			<a href="/auth">${m.getString("msg.vocabs")}</a>
		</div>
		<#include "message.ftl">
		<section>
			<h3>Search results</h3>
			<section>
				<table>
					<tr><th>ID</th><th>Label</th></tr>
					<#list results as r>
						<tr><td><a href="${r.id}">${r.id}</a></td><td>
						<#list langs as lang>
							<#list r.getPrefLabels(lang) as val>
								${val}<br>
								</#list>
						</#list>
						</td></tr>
					</#list>
				</table>
			</section>
		</section>
	</section>
	</div>
</main>
<#include "footer.ftl">
</body>
</html>
