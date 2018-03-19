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
<#assign s = shacl.shapes>
<main>
    <div id="container">
    <#include "message.ftl">
    <#if s?has_content>
    <section>
        <h3>Node Shapes</h3>
        <#list s?sort_by("id") as ns>
            <tr><td>${ns.id}</td></tr>
        </#list>
    </section>
    </#if>
    </div>
</main>
<#include "footer.ftl">
</body>
</html>
