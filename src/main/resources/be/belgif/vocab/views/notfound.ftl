<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8'>
<link rel="stylesheet" type="text/css" href="/static/style.css" />
<title>Vocab Belgif - DEMO</title>
</head>
<body>
<#include "header.ftl">
<#assign m = messages>
<main>
  <section>
	<h3>${m.getString("msg.notfound")}</h3>
  </section>
</main>
<#include "footer.ftl">
</body>
</html>
