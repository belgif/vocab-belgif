<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8'>
<link rel="stylesheet" type="text/css" href="/static/style.css" />
<#include "title.ftl">
</head>
<body>
<#include "header.ftl">
<#assign l = lang>
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
					<tr><td><a href="${c.id}">${c.getTitle(l)!""}</a></td>
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
