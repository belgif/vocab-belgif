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
	    <#assign n = v.notation!"">
	    <#if n?has_content>
		<tr><td>SKOS notation</td><td>${n}</td></tr>
	    </#if>
	    <#assign labels = [ "prefLabel", "altLabel" ]>
	    <#list labels as label>
		<#list langs as lang>
		    <#assign val = v.literal("skos", label, lang)!"">
		    <#if val?has_content>
			<tr><td>SKOS ${label} (${lang})</td><td>${val}</td></tr>
		    </#if>
		</#list>
	    </#list>
	    </table>
        </section>
        <section>
	    <h4>Matches</h4>
	    <table>
	    <#list v.sameAs as s>
		<tr><td>OWL sameAs</td><td><a href="${s}">${s}</a></td></tr>
	    </#list>
	    <#assign refs = [ "exactMatch", "closeMatch", "broadMatch", "narrowMatch" ]>
	    <#list refs as ref>
		<#list v.objs("skos", ref)!"" as val>
		    <tr><td>SKOS {ref}</td><td><a href="${val}">${val}</a></td></tr>
		</#list>
	    </#list>
	    </table>
	</section>
	<#list [ "narrower", "broader" ] as bn>
	    <#assign rels = v.objs("skos", bn)>
	    <#if rels?has_content>
	    <section>
		<h4>SKOS ${bn}</h4>
		<table>
		<#list rels as rel>
		    <tr><td><a href="${rel}">${rel}</a></td></tr>
		</#list>
		</table>
	    </section>
	    </#if>
	</#list>
    </section>
</main>
<footer>
	&copy; 2017 <a href="http://www.fedict.belgium.be">Fedict</a>
</footer>
</body>
</html>
