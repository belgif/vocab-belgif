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
    <div id="container">
    <section>
	<h3>Thesauri</h3>
	<section>
	    <h4>Overview</h4>
	    <table>
	    <tr><th>Name</th>
		<th>Description</th>
		<th>Download</th>
	    </tr>
	    <#list vocabs as v>
	    <tr><td><a href="${v.root}">${v.literal("dcterms", "title", "en")!""}</a></td>
		<td>${v.literal("dcterms", "description", "en")!""}</td>
		<td><a href="${v.download!""}.ttl">TTL</a>
		    <a href="${v.download!""}.jsonld">JSON-LD</a>
		    <a href="${v.download!""}.nt">N-Triples</a></td>
	    </tr>
	    </#list>
	    </table>
	</section>
	<section>
	    <h4>Content Negotiation</h4>
	    <table>
	    <tr><th>Format</th><th>HTTP Accept:</th></tr>
	    <tr><td>HTML</td><td>text/html</td></tr>
	    <tr><td>JSON-LD</td><td>application/ld+json</td></tr>
	    <tr><td>N-Triples</td><td>application/n-triples</td></tr>
	    <tr><td>Turtle</td><td>text/turtle</td></tr>
	    </table>
	</section>
    </section>
    </div>
</main>
<footer>
	&copy; 2017 <a href="http://www.fedict.belgium.be">Fedict</a>
</footer>
</body>
</html>
