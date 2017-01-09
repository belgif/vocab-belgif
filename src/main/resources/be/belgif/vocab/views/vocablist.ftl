<#-- @ftlvariable name="" type="be.belgif.vocab.views.VocabListView" -->
<!DOCTYPE html>
<html>
    <head>
	<meta charset='UTF-8'>
	<title>Vocabularies Belgif.be</title>
    </head>
    <body>
        <h1>Vocab Belgif.be</h1>
	<table>
	    <#list vocabs as v>
	    <tr><th>${v.name}</th><td></td><td></td></tr>
	    </#list>
	</table>
    </body>
</html>
