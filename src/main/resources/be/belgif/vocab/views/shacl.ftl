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
<#assign sh = shacl>
<#assign s = sh.shapes>
<main>
    <div id="container">
    <#include "message.ftl">
    <#if s?has_content>
    <section>
        <h3>Prefixes</h3>
        <table>
            <tr><th>Prefix</th><th>Namespace</th></tr>
            <#assign ns = sh.usedNamespaces>
            <#list ns?keys?sort as pref> 
                <tr><td>${pref}</td><td>${ns[pref]}</td></tr>
            </#list>
        </table>
    </section>
    <section>
        <h3>Node Shapes</h3>
        <#list s?sort_by("id") as ns>
            <table class="shacl">
            <#assign target = ns.target!"">
            <tr><th colspan="3"><a href="${target}">${sh.getShort(target)}</a></th></tr>
            <#assign props = ns.propertyShapes>
            <#list props as prop>
                <#assign path = prop.path!"">
                <tr>
                    <td><a href="${path}">${sh.getShort(path)}</a></td>
                    <td>${prop.minCount!"0"} - ${prop.maxCount!"N"}</td>
                    <#assign typ = prop.dataType!prop.shClass!"">
                    <td><a href="${typ}">${sh.getShort(typ)}</a></td>
                </tr>
            </#list>
            </table>
        </#list>
    </section>
    </#if>
    </div>
</main>
<#include "footer.ftl">
</body>
</html>
