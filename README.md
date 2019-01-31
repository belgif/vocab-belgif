# lod-vocab

Publication tool for publishing XML XSD's, SKOS thesauri, RDFS/OWL ontologies 
and SHACL validation rules from [lod-vocab-data](https://github.com/fedict/lod-vocab-data) on Vocab.belgif.be.


Best practices and guidelines can be found in [README-SKOS.md](README-SKOS.md) and [README-RDFSOWL.md](README-RDFSOWL.md)

# Description

The application is a simple [Dropwizard](http://dropwizard.io) application including an [RDF4j](http://rdf4j.org) triple store.

Using HTTP Content Negotiation, the application will either show human-friendly HTML or send a machine-readable RDF representation (JSON-LD, N-Triples or Turtle)


# Configuration

Configuration is done using a YAML file, an example is included in the source.

## Subdirectories

Both Lucene and RDF4J triplestore need (separate) writable directories. 
In addition, there must be a directory for importing the thesauri, 
and another one for the generated dump files in several formats.

The directories must be created before starting the application, the exact location can be configured in the YAML file.

# Admin tasks

## Publishing / updating thesauri

One can import a new thesauri using a valid [SKOS](https://www.w3.org/TR/skos-primer/) file in N-Triples or Turtle format.
Note that the `skos:ConceptScheme` must at least have an English `dcterms:title` and `dcterms:description`,
as they will be used by the HTML viewer.

Importing and updating thesauri is done by putting the SKOS file into the import directory `importDir`
and running `POST`ing the data to the import URL.
The `name` parameter must contain the file name of the SKOS file to be imported.

```
curl -X POST --data "name=newthes.nt" http://localhost:8081/tasks/vocab-import
```

The file name will be used to construct an RDF4j Context / named graph

The import task will add some [VoID](https://www.w3.org/TR/void/) statistics and metadata,
and generate dump files in several formats (JSON-LD, N-Triples and Turtle) in the `downloadDir`

## Re-indexing Lucene

Run the `lucene-reindex` task using HTTP PUT, e.g.

```
curl -X POST http://localhost:8081/tasks/lucene-reindex
```

The index is stored in the `luceneDir`
