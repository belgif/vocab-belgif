# Vocab.belgif.be

Publishing SKOS vocabularies

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

## Benefits

- Simple to create and use
- Used by EU Publication Office, UN FAO, ...

---

## Vocab server

- HTML view on taxonomies
- Download files (multiple formats)
- Linked Data Fragments API

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

---

## Thank you

Questions ? 

[opendata@belgium.be](mailto:opendata@belgium.be)