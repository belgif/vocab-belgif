# Vocab.belgif.be

```
Publishing re-usable thesauri as Linked Open Data
```

---

## Vocab server

- Publishing XSD schema's
- Publishing thesauri, controlled lists

---

## Thesauri

- Searchable HTML view on taxonomies, controlled lists
- Multiple download formats (JSON-LD, TTL...)
- Linked Data Fragments API for machines

---
## What is SKOS ?

- [Simple Knowledge Organization System](https://www.w3.org/2004/02/skos/)
- RDF-based W3C Recommendation
- For taxonomies, thesauri, code lists

+++

## Features

- Terms can have labels in multiple languages
- Preferred, alternative labels
- Hierarchy: broader, narrower terms
- Mapping =/= taxonomies: close, exact match

+++

## Example

```

```

+++

## Benefits

- Simple to create and use
- Used by EU Publication Office, UN FAO, ...
- Possible to link to other thesauri

---

## Technology

- DropWizard server
- Embedded RDF4J triple store
- Deployed as 1 docker container

+++

## What is DropWizard ?

- Open source Java [REST framework](http://www.dropwizard.io)
  - Jetty server, Jersey annotations, Freemarker templates
- Stand-alone micro-services

+++

## What is RDF4J ?

- Eclipse [open source library](http://rdf4j.org/)
- RDF API, library, tools and triple store
- OntoText [GraphDB](https://ontotext.com/products/graphdb/), 
[Halyard](https://github.com/Merck/Halyard)

+++

## Linked Data Fragments API

- [Triple Pattern Fragments](http://linkeddatafragments.org/) "web-scale" queries
- Client-side SPARQL queries
- Paginated results
- Very basic queries on the server
  - Less server load, but more network traffic

---

## Creating SKOS files

- Manually in a text editor
- Quick and dirty conversion
- Special tools

+++

## Why not

- Taxonomies often already maintained in other systems

---

## Adding a new thesauri

- Create a new SKOS file
  - With dcterms:title, dcterms:description in NL-FR-DE-EN
- Add it to the github repository
- Build and deploy docker image
  - Files will be indexed on start-up

---

## Thank you

Questions ? 

@fa[twitter] @BartHanssens

@fa[envelope] [opendata@belgium.be](mailto:opendata@belgium.be)