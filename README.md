# lod-vocab

SKOS publication tool for publishing demo thesauri on Vocab.belgif.be

These thesauri will change without prior notice, there are no guarantees on availability and correctness.

# Description

A Dropwizard application including an RDF4j triple store.

# Configuration

Configuration is done using a YAML file.

## Subdirectories

Both Lucene and RDF4j triplestore need (separate) writable directories. 
In addition, ther must be a directory for importing the thesauri, 
and another one for the generated dump files in several formats.

# Admin tasks

## Importing / updating thesauri

Note: the SKOS concept must at least have an English `dcterms:title` and a `dcterms:description`.

Importing and updating thesauri is done by putting the SKOS file into the import directory
and running the import task using HTTP POST.
The `name` parameter must contain the file name of the SKOS file to be imported.

```
curl -X POST --data="name=newthes.nt" http://localhost:8081/tasks/vocab-import
```

Note that the file name will be used to construct an RDF4j Context / named graph

The import task will add some VoID statistics and metadata,
and generate dump files in several formats (JSON-LD, N-Triples and Turtle)

## Re-indexing Lucene

Run the `lucene-reindex` task using HTTP PUT, e.g.

```
curl -X POST http://localhost:8081/tasks/lucene-reindex
```