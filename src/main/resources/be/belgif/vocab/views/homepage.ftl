<#assign l = lang>
<!DOCTYPE html>
<html lang="${l}">
<head>
<meta charset='UTF-8'>
<link rel="alternate" type="text/turtle" href="/void" />
<link rel="stylesheet" type="text/css" href="/static/style.css" />
<link rel="stylesheet" type="text/css" href="/static/bootstrap.min.css" />
<link rel="icon" type="image/x-icon" href="/static/favicon.ico" />
<script src="/static/jquery.min.js"></script>
<script src="/static/bootstrap.bundle.min.js"></script>
<#include "title.ftl">
</head>
<body class="homepage">
<#include "header.ftl">
<#assign m = messages>
<main class="container-fluid">
	<div class="row row-cols-1 row-cols-md-2">
		<div class="col mb-2">
		<section class="card be-eif">
			<div class="card-body">
				<h1 class="card-title"><i class="fas fa-cubes"></i>
					<a href="/auth?lang=${l}" class="stretched-link text-decoration-none">
					${m.getString("msg.vocabs")}</a>
				</h1>
				<p class="lead">${m.getString("msg.vocabsdesc")}</p>
			</div>
		</section>
		</div>
		<div class="col mb-2">
		<section class="card be-specs h-100">
			<div class="card-body">
				<h1 class="card-title"><i class="far fa-file-code"></i>
					<a href="/ns?lang=${l}" class="stretched-link text-decoration-none">
					${m.getString("msg.ontos")}</a> 
				</h1>
				<p class="lead">${m.getString("msg.ontosdesc")}</p>
			</div>
		</section>
		</div>
		<div class="col mb-2">
		<section class="card be-activities h-100">
			<div class="card-body">
				<h1 class="card-title"><i class="fas fa-users"></i>
					<a href="/shacl?lang=${l}" class="stretched-link text-decoration-none">
					${m.getString("msg.shacls")}</a>
				</h1>
				<p class="lead">${m.getString("msg.shaclsdesc")}</p>
			</div>
		</section>
		</div>
		<div class="col mb-2">
		<section class="card be-legal h-100">
			<div class="card-body">
				<h1 class="card-title"><i class="fas fa-balance-scale"></i> 
					<a href="/ctx?lang=${l}" class="stretched-link text-decoration-none">
					${m.getString("msg.ctxs")}</a>
				</h1>
				<p class="lead">${m.getString("msg.ctxsdesc")}</p>
			</div>
		</section>
		</div>
	</div>
</main>
<#include "footer.ftl">
</body>
</html>
