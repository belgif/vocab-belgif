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
    <#assign ns = s.usedNs>
    <#if ns?hasContent>
    <section>
        <h3>Prefixes</h3>
        <table>
            <tr><th>Prefix</th><th>Namespace</th>/tr>
            <<#list ns.entrySet?sort as e> 
                <tr><td>${e.key}</td><td>${e.value}</td></tr>
            </#list>
        </table>
    </section>
    </#if>
    <section>
        <h3>Node Shapes</h3>
        <#list s?sort_by("id") as ns>
            <table>
            <tr><th colspan="3">${ns.shortTarget}</th></tr>
            <#assign props = ns.propertyShapes>
            <#if props?has_content>
                <#list props as prop>
                    <tr><td>${prop.shortPath}</td>
                        <td>${prop.minCount!"0"} - ${prop.maxCount)!"N"}</td>
                        <td>${prop.shortType!prop.shortClass}</td>
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
