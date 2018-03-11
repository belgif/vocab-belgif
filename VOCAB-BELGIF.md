# Requirements for SKOS taxonomies

In order to be published on vocab.belgif.be, the following requirements must be met

## File
 * the file must be a valid [SKOS](https://www.w3.org/2009/08/skos-reference/skos.html) file
 * UTF-8 encoding must be used
 * RDF N-Triples serialization is recommended but not required (Turtle and JSON-LD are acceptable as well)
 
## SKOS Concept Scheme
 * URI must adhere to the template: http://vocab.belgif.be/auth/[short-taxonomy-name]#id
    * where short name is all-lowercase, alphanumeric characters and hyphens are permitted
 * skos:ConceptScheme must have a dcterms:title in Dutch, French, English and German (will be used in the HTML overview)
 * skos:ConceptScheme must have a dcterms:description in Dutch, French, English and German (will be used in the HTML overview)

## SKOS Concept
 * URI must adhere to the template: http://vocab.belgif.be/auth/[short-taxonomy-name]/[term-code]#id
 * skos:Concepts must have skos:prefLabel in 4 languages
 * skos:Concepts should have one skos:notation
 * skos:Concepts may have multiple skos:altLabel, skos:note and/or skos:definition
 * skos:Concepts may have a schema:startDate and schema:endDate to indicate the validity of the term throughout time

## SKOS hierarchy and relations
 * skos:hasTopConcept and its inverse skos:topConceptOf must be used to explicitly link the Concept Scheme to the top level terms
   * if the taxonomy is "flat" (i.e. there are no children), every skos:Concept is a top level term
 * if the taxonomy is a tree-like structure, skos:narrower and its inverse skos:broader must be used to explicitly link parent terms to their children and vice-versa
 * skos:Concepts may be linked to terms in external taxonomies using one or more skos:exactMatch, skos:closeMatch, skos:broadMatch and/or skos:narrowMatch
