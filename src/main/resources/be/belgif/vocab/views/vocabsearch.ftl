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
<#assign langs = ['nl', 'fr', 'en', 'de']>
<main>
    <div id="container">
    <#include "message.ftl">
    <section>
	<h3>Search results</h3>
	<section>
	    <table>
	    <tr><th>ID</th><th>Label</th></tr>
	    <#list results as r>
		<tr><td><a href="${r.id}">${r.id}</a></td><td>
		<#list langs as lang>
                    <#assign val = r.getPrefLabel(lang)>
		    <#if val?has_content>
			${val}<br>
		    </#if>
		</#list>
		</td></tr>
	    </#list>
	    </table>
	</section>
    </section>
    </div>
</main>
<#include "footer.ftl">
</body>
</html>
