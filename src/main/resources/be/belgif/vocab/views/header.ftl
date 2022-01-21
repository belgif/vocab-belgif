<#assign l = lang>
<#assign m = messages>
<header>
	<nav id="lang-wrapper" class="nav nav-pills justify-content-end">
	<#list ['nl', 'fr', 'en', 'de'] as lang>
		<a href="?lang=${lang}" hreflang="${lang}" class="nav-link ${l?switch(lang, "active", "")}">${lang?upper_case}</a>
	</#list>
	</nav>
	<nav class="navbar navbar-expand-lg navbar-light">
		<a href="/?lang=${l}" class="navbar-brand">
			<img id="logo" src="/static/belgif.png" alt="Belgif logo"/>
		</a>
		<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#menu" 
				aria-controls="menu" aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div id="menu" class="collapse navbar-collapse justify-content-end" >
			<a class="nav-link font-weight-bold" href="/?lang=${l}">${m.getString("msg.home")}</a>
			<a class="nav-link font-weight-bold" href="/auth?lang=${l}">${m.getString("msg.vocabs")}</a>
			<a class="nav-link font-weight-bold" href="/ns?lang=${l}">${m.getString("msg.ontos")}</a>
			<a class="nav-link font-weight-bold" href="/shacl?lang=${l}">${m.getString("msg.shacls")}</a>
			<a class="nav-link font-weight-bold" href="/ctx?lang=${l}">${m.getString("msg.ctxs")}</a>
			<a class="nav-link font-weight-bold" href="/contact?lang=${l}">${m.getString("msg.contact")}</a>
		</div>
	</nav>
</header>
