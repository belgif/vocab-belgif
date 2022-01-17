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
		<#if contexts?has_content>
		<section>
			<h3>${m.getString("msg.ctxs")}</h3>
			<section>
				<h4>${m.getString("msg.overview")}</h4>
				<table>
					<tr><th>${m.getString("msg.name")}</th>
						<th>${m.getString("msg.description")}</th>
						<th>${m.getString("msg.downloads")}</th>
					</tr>
				<#list contexts as c>
					<tr><td>${c.getTitle(l)!""}</td>
						<td>${c.getDescription(l)!""}</td>
						<td><a href="${c.download!""}">JSON-LD</a></td>
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
