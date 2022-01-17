<#assign l = lang>
<!DOCTYPE html>
<html lang="${l}">
<head>
<meta charset='UTF-8'>
<link rel="stylesheet" type="text/css" href="/static/style.css" />
<link rel="icon" type="image/x-icon" href="/static/favicon.ico" />
<link rel="alternate" type="text/turtle" href="/void" />
<#include "title.ftl">
</head>
<body>
<#include "header.ftl">
<#assign m = messages>
<main>
	<div id="container">
	<section>
		<#include "message.ftl">
		<section>
			<h3>${m.getString("msg.vocabs")}</h3>
			<p><a href="/auth">${m.getString("msg.vocabsdesc")}</a></p>
		</section>
		<section>
			<h3>${m.getString("msg.ontos")}</h3>
			<p><a href="/ns">${m.getString("msg.ontosdesc")}</a></p>
		</section>
		<section>
			<h3>${m.getString("msg.shacls")}</h3>
			<p><a href="/shacl">${m.getString("msg.shaclsdesc")}</a></p>
		</section>
		<section>
			<h3>${m.getString("msg.ctxs")}</h3>
			<p><a href="/ctx">${m.getString("msg.ctxsdesc")}</a></p>
		</section>
	</section>
	</div>
</main>
<#include "footer.ftl">
</body>
</html>
