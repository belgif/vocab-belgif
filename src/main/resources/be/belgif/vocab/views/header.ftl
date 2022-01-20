<#assign l = lang>
<#assign m = messages>
<header>
	<nav id="lang-wrapper" class="nav nav-pills justify-content-end">
		<a href="?lang=nl" hreflang="nl" class="nav-link active">NL</a>
		<a href="?lang=fr" hreflang="fr" class="nav-link ">FR</a>
		<a href="?lang=de" hreflang="de" class="nav-link ">DE</a>
		<a href="?lang=en" hreflang="en" class="nav-link ">EN</a>
	</nav>
	<nav class="navbar navbar-expand-lg navbar-light bg-light">
		<a href="/?lang=${l}" class="navbar-brand">
			<img id="logo" src="/static/belgif.png" alt="Belgif logo">
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
		</div>
	</nav>
</header>
