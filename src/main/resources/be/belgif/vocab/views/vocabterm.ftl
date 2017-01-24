<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8'>
<link rel="stylesheet" type="text/css" href="/static/style.css" />
<title>Belgif - Vocabularies DEMO</title>
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
	<li class="disabled">nl</li>
	<li class="disabled">fr</li>
	<li class="disabled">de</li>
	<li class="disabled">en</li>
    </ul>
    </div>
    <div id="flag"></div>
    </div>
    <div id="site-name">
	<h1 class="site-name">Belgif</h1>
	<h2 class="slogan">Linked Open Data Thesauri DEMO</h2>
    </div>
</header>
<main>
    <#assign v = term>
    <#assign langs = ['nl', 'fr', 'en', 'de']>
    <section>
	<h3>${v.id}</h3>
	<section>
	    <h4>Search</h4>
	    <form method="get" action="/${vocab}/_search">
		<input name="q" type="search"/>
		<input name="search" type="submit"/>
	    </form>
	</section>
	<section>
	    <h4>General</h4>
	    <table>
	    <#list langs as lang>
		<#assign val = v.literal("dcterms", "description", lang)!"">
		<#if val?has_content>
		    <tr><td>DCTERMS description (${lang})</td><td>${val}</td></tr>
		 </#if>
	    </#list>
	    <#assign l = v.notation!"">
	    <#if l?has_content>
		<tr><td>SKOS notation</td><td>${l}</td></tr>
	    </#if>
	    <#list langs as lang>
		<#assign val = v.literal("skos", "prefLabel", lang)!"">
		<#if val?has_content>
		    <tr><td>SKOS prefLabel (${lang})</td><td>${val}</td></tr>
		</#if>
	    </#list>
	    <#list langs as lang>
		<#assign val = v.literal("skos", "altLabel", lang)!"">
		<#if val?has_content>
		    <tr><td>SKOS altLabel (${lang})</td><td>${val}</td></tr>
		</#if>
	    </#list>
	    <#assign sa = v.exactMatch>
	    <#if sa?size gt 0>
		<#list sa as s>
		    <tr><td>SKOS exactMatch</td><td><a href="${s}">${s}</a></td></tr>
		</#list>
	    </#if>
	    <#assign sa = v.sameAs>
	    <#if sa?size gt 0>
		<#list sa as s>
		    <tr><td>OWL sameAs</td><td><a href="${s}">${s}</a></td></tr>
		</#list>
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
	<#assign tc = v.narrower>
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
	<#assign tc = v.broader>
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
