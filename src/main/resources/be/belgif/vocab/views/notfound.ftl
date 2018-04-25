<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8'>
<link rel="stylesheet" type="text/css" href="/static/style.css" />
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
			<h3>${m.getString("msg.notfound")}</h3>
		</section>
	</section>
	</div>
</main>
<#include "footer.ftl">
</body>
</html>
