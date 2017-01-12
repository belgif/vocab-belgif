<#-- @ftlvariable name="" type="be.belgif.vocab.views.VocabTermView" -->
<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8'>
<link rel="stylesheet" type="text/css" href="/static/style.css" />
<title>Belgif - Vocabularies</title>
</head>
<body>
<header>
    <div id="logo-wrapper">
    <a href="index.html">
	<img id="logo" src="/static/belgif.png" alt="Belgif logo"/>
    </a>
    </div>
    <div id="lang-wrapper">
    <div id="languages">
    <ul>
	<li><a href="?lang=nl">nl</a></li>
	<li><a href="?lang=fr">fr</a></li>
	<li><a href="?lang=de">de</a></li>
	<li class="disabled">en</li>
    </ul>
    </div>
    <div id="flag"></div>
    </div>
    <div id="site-name">
	<h1 class="site-name">Belgif</h1>
	<h2 class="slogan">Linked Open Data Thesauri</h2>
    </div>
</header>
<main>
    <#assign v = term>
    <section>
	<h3>${v.id}</h3>
	<section>
	    <h4>General</h4>
	    <table>
	    <#assign l = v.literal("dcterms", "description", "en")!>
	    <#if l?has_content>
		<tr><td>DCTERMS description</td><td>${l}</td></tr>
	    </#if>
	    <#assign l = v.literal("skos", "notation", "")!>
	    <#if l?has_content>
		<tr><td>SKOS notation</td><td>${l}</td></tr>
	    </#if>
	    <#assign l = v.literal("skos", "prefLabel", "en")!>
	    <#if l?has_content>
		<tr><td>SKOS prefLabel</td><td>${l}</td></tr>
	    </#if>
	    </table>
	</section>
	<#assign tc = v.objs("skos", "hasTopConcept")>
	<#if tc?size gt 0>
	<section>
	    <h4>SKOS TopConcepts</h4>
	    <table>
	    <#list tc as t>
		<tr><td><a href="${t}">${t}</a></td></tr>
	    </#list>
	    </table>
	</section>
	</#if>
	<#assign tc = v.objs("skos", "narrower")>
	<#if tc?size gt 0>
	<section>
	    <h4>SKOS narrower</h4>
	    <table>
	    <#list tc as t>
		<tr><td><a href="${t}">${t}</a></td></tr>
	    </#list>
	    </table>
	</section>
	</#if>
	<#assign tc = v.objs("skos", "broader")>
	<#if tc?size gt 0>
	<section>
	    <h4>SKOS broader</h4>
	    <table>
	    <#list tc as t>
		<tr><td><a href="${t}">${t}</a></td></tr>
	    </#list>
	    </table>
	</section>
	</#if>
    </section>
</main>
<footer>
	&copy; 2017 <a href="http://www.fedict.belgium.be">Fedict</a>
</footer>
</body>
</html>
