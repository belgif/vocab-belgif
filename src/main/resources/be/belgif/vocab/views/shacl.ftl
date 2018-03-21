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
            <table>
            <tr><th colspan="3">${ns.shortTarget}</th></tr>
            <#assign props = ns.propertyShapes>
            <#if props?has_content>
                <#list props as prop>
                    <tr><td>${prop.shortPath}</td>
                        <td>${prop.obj("sh", "minCount")!"0"} -
                        ${prop.obj("sh", "maxCount")!"N"}
                        <#assign type = prop.obj("sh", "datatype")!"">
                        <#if ! type?has_content>
                            <#assign type = prop.obj("sh", "class")!"">
                        </#if>
                        <td>${type}</td>
                    </tr>
                </#list>
            </#if>
            </table>
        </#list>
    </section>
    </#if>
    </div>
</main>
<#include "footer.ftl">
</body>
</html>
