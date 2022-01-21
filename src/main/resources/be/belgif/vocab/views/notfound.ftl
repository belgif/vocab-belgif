<#assign l = lang>
<!DOCTYPE html>
<html lang="${lang}">
<#include "head.ftl">
<body>
<#include "header.ftl">
<#assign m = messages>
<main class="container-fluid bg-light">
	<div class="alert alert-warning" role="alert">
		${m.getString("msg.notfound")}
	</div>
</main>
<#include "footer.ftl">
</body>
</html>
