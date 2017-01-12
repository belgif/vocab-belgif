<#-- @ftlvariable name="" type="be.belgif.vocab.views.VocabListView" -->
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
    <div id="container">
    <section class="panel">
	<table>
	<tr><th>Name</th>
	    <th>Description</th><th>Download (TTL)</th>
	    <th>Download (JSON)</th></tr>
	<#list vocabs as v>
	<tr><td><a href="${v.url}">${v.literal("dcterms","title", "nl")}</a></td>
	    <td>${v.literal("dcterms","description", "nl")}</td><td></td></tr>
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
