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
    <a href=".">
	<img id="logo" src="/static/belgif.png" alt="Belgif logo"/>
    </a>
    </div>
    <div id="lang-wrapper">
    <div id="languages">
    <ul>
	<li><a href="index.nl">nl</a></li>
	<li><a href="index.fr">fr</a></li>
	<li><a href="index.de">de</a></li>
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
    <div id="container">
    <section class="panel">
	<table>
	<tr><th>Name</th>
	    <th>Description</th>
	    <th>Download (TTL)</th>
	    <th>Download (JSON)</th>
	</tr>
	<#list vocabs as v>
	<tr><td><a href="${v.root}">${v.literal("dcterms", "title", "en")!""}</a></td>
	    <td>${v.literal("dcterms", "description", "en")!""}</td>
	    <td><a href="${v.download!""}.ttl">${v.url!""}.ttl</td>
	    <td><a href="${v.download!""}.json">${v.url!""}.json</td>
	</tr>
	</#list>
	</table>
    </section>
    </div>
</main>
<footer>
	&copy; 2017 <a href="http://www.fedict.belgium.be">Fedict</a>
</footer>
</body>
</html>
