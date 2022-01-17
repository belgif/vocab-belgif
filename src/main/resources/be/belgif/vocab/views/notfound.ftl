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
