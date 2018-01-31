# Vocab.belgif.be


> Publishing re-usable thesauri as Linked Open Data

---

## Vocab server

- Publishing XSD schema's
- Publishing thesauri, controlled lists

---

## Thesauri

- Searchable HTML view
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

```xml
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

## Published

- NACEbel economic activities
- Territories recognized by Belgium
   - linked to GeoNames, EU country codes
- NIS regions and municipalities
  - linked to GeoNames
- ...

---

## Adding a new thesauri

- Create a new SKOS file
  - dcterms:title, dcterms:description (NL-FR-DE-EN)
- Add it to the github repository
- Build and deploy new instance
  - Files will be indexed and converted on start-up

---

## Creating SKOS files

- Manually in a text editor (Turtle syntax)
- Basic [Excel-to-SKOS](https://github.com/Fedict/lod-skosifier) conversion
- Specialized tools
  - [Skosmos](http://skosmos.org/), [Vocbench](http://vocbench.uniroma2.it/)

+++

## Why conversion

- Taxonomies often maintained in other systems
- No need for yet another input tool

---

## Technology

- DropWizard server
- Embedded RDF4J triple store
- Deployed as 1 docker container
  - Read-only, scalable

+++

## What is DropWizard ?

- Open source Java [REST framework](http://www.dropwizard.io) by Yammer
  - Jersey annotations, Freemarker templates
- Stand-alone micro-services

+++

## What is RDF4J ?

- Java [open source library](http://rdf4j.org/)
- RDF API, library, tools and triple store
- Used by OntoText [GraphDB](https://ontotext.com/products/graphdb/), 
[Halyard](https://github.com/Merck/Halyard)

---

## Linked Data Fragments API

- [Triple Pattern Fragments](http://linkeddatafragments.org/) "web-scale" queries
- Client-side SPARQL queries
- Paginated results
- Very basic queries on the server
  - Less server load, more network traffic

---

## Thank you

Questions ? 

@fa[twitter] @BartHanssens

@fa[envelope] [opendata@belgium.be](mailto:opendata@belgium.be)