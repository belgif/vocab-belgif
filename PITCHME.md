# Vocab.belgif.be


> Publishing re-usable thesauri as Linked Open Data

---

## Vocab server

- Publishing XSD schema's
- Publishing thesauri, controlled lists

---

## Thesauri

- Searchable HTML view
  - NACEbel, NIS, Territories, Be themes
- SKOS in multiple download formats (JSON-LD, TTL...)
- Linked Data Fragments API for machines

---
## What is SKOS ?

- [Simple Knowledge Organization System](https://www.w3.org/2004/02/skos/)
- RDF-based W3C Recommendation
- Describing taxonomies, thesauri, code lists

+++

## Features

- Terms can have labels in multiple languages
- Preferred, alternative labels
- Hierarchy: broader, narrower terms
- Mapping =/= taxonomies: close, exact match

+++

## Example

```
@prefix skos: <http://www.w3.org/2004/02/skos/core#> .

<http://vocab.belgif.be/auth/mybe-theme/FINA#id> a skos:Concept;
    skos:notation "FINA" ;
    skos:prefLabel "Financiën"@nl, "Finance"@fr ;
    skos:broadMatch <http://publications.europa.eu/resource/authority/data-theme/ECON> .

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

![Docker logo](https://www.docker.com/sites/default/files/vertical.png?height=10%&size=auto 10%)
![RDF4J logo](http://rdf4j.org/wp-content/uploads/2016/04/rdf4j-logo-orange-114.png&size=auto 10% }

+++

## What is DropWizard ?

- Open source Java [REST framework](http://www.dropwizard.io) by Yammer
  - Jersey annotations, Freemarker templates
- Stand-alone micro-services

![Docker logo](https://www.docker.com/sites/default/files/vertical.png)

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
- Quick and dirty [Excel-to-SKOS](https://github.com/Fedict/lod-skosifier) conversion
- Specialized tools
  - [Skosmos](http://skosmos.org/), [Vocbench](http://vocbench.uniroma2.it/)

+++

## Why not

- Taxonomies often already maintained in other systems

---

## Adding a new thesauri

- Create a new SKOS file
  - dcterms:title, dcterms:description (NL-FR-DE-EN)
- Add it to the github repository
- Build and deploy docker image
  - Files will be indexed on start-up

---

## Thank you

Questions ? 

@fa[twitter] @BartHanssens

@fa[envelope] [opendata@belgium.be](mailto:opendata@belgium.be)