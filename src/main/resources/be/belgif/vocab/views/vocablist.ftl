<#assign l = lang>
<!DOCTYPE html>
<html lang="${lang}">
<#include "head.ftl">
<body>
<#include "header.ftl">
<#assign m = messages>
<#assign vs = vocabs>
<main>
	<div id="container">
	<section>
		<div id="breadcrumb">
			<a href="/">${m.getString("msg.home")}</a>
		</div>
		<#include "message.ftl">
		<#if vs?has_content>
		<section>
			<h3>Thesauri</h3>
			<section>
				<h4>${m.getString("msg.overview")}</h4>
				<table>
					<tr><th>${m.getString("msg.name")}</th>
						<th>${m.getString("msg.description")}</th>
						<th>${m.getString("msg.downloads")}</th>
					</tr>
					<#list vs?sort_by("id") as v>
					<tr><td><a href="${v.id}">${v.getTitle(l)!""}</a></td>
						<td>${v.getDescription(l)!""}</td>
						<#assign download = v.download!"">
						<td><a href="${download}.ttl">TTL</a>
							<a href="${download}.jsonld">JSON-LD</a>
							(<a href="${download}-framed.jsonld">framed</a>)
							<a href="${download}.nt">N-Triples</a></td>
					</tr>
					</#list>
				</table>
			</section>
		</section>
		<section>
			<#include "contentneg.ftl">
			<section>
				<h4>Linked Data Fragments</h4>
				<p>http://vocab.belgif.be/_ldf</p>
			</section>
		</section>
		</#if>
	</section>
	</div>
</main>
<#include "footer.ftl">
</body>
</html>
