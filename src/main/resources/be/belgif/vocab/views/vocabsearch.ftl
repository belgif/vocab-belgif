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
	<h2 class="slogan">Linked Open Data Thesauri</h2>
    </div>
</header>
<main>

    <#assign langs = ['nl', 'fr', 'en', 'de']>
    <section>
	<h3>Search results</h3>
	<section>
	    <table>
	    <#list results as r>
		<tr><td>${r.id}</td><td></tr>
	    </#list>
	    </table>
	</section>
    </section>
</main>
<footer>
	&copy; 2017 <a href="http://www.fedict.belgium.be">Fedict</a>
</footer>
</body>
</html>
