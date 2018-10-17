# Requirements for RDFS / OWL ontologies

In order to be published on vocab.belgif.be, the following requirements must be met

## File
 * the file must be a valid [OWL2](https://www.w3.org/2009/08/skos-reference/skos.html) file
 * UTF-8 encoding must be used
 * RDF Turtle serialization is recommended but not required (NTriples and JSON-LD are acceptable as well)
 
## owl:Ontology
 * URI must adhere to the template: http://vocab.belgif.be/ns/[short-ontology-name]#
    * where short-ontology-name is all-lowercase, only alphanumeric characters and hyphens are permitted
    * short-ontology-name can be based on English words (e.g. abbreviation), but should be relatively short
    * "translating" short-ontology-name is not permitted (translation must be done at the label level)
 * owl:Ontology should have a owl:versionInfo description
 * owl:Ontology must have a rdfs:label in Dutch, French, English and German (will be used in the HTML overview as title)
 * owl:Ontology must have a rdfs:comment in Dutch, French, English and German (will be used in the HTML overview as description)

## Classes
 * URI must adhere to the template: http://vocab.belgif.be/ns/[short-ontology-name]#[ClassName]
   * where ClassName written in "camel case", only alphanumeric characters are permitted
   * the first character of ClassName is always an uppercase letter
   * ClassNames must be based on English words, but should be relatively short
   * "translating" ClassNames is not permitted (translation must be done at the label level)
 * classes must have a rdfs:isDefinedBy
 * classes must have a rdfs:label in Dutch, French, English and German (will be used in the HTML overview) 
 * classes may have a rdfs:comment in Dutch, French, English and German (will be used in the HTML overview)
 * classes may be a rdfs:subClassOf of another class
 * classes must be of rdf:type rdfs:Class and owl:Class
 
## Properties
 * URI must adhere to the template: http://vocab.belgif.be/ns/[short-ontology-name]#[propertyName]
   * where propertyName is "camel case" (except for the first word), only alphanumeric characters are permitted
   * the first character of propertyName is always a lowercase letter
   * propertyNames must be based on English words, but should be relatively short
   * "translating" propertyNames is not permitted (translation must be done at the label level)
   * propertyNames must not be based on classes mentioned in the rdfs:Range or rdfs:Domain properties
 * properties must have a rdfs:isDefinedBy
 * properties must have a rdfs:label in Dutch, French, English and German (will be used in the HTML overview)
 * properties may have a rdfs:comment in Dutch, French, English and German (will be used in the HTML overview)
 * properties must be of rdf:type rdf:Property and (owl:ObjectProperty or owl:DataProperty)
 * properties may be a rdfs:subPropertyOf of another property
 * properties may have one or more rdfs:Domain
 * properties may have one or more rdfs:Range

## Other OWL capabilities (cardinality etc)
 * Ontologies may include other constraints and definitions (e.g. owl:disjointWith)
 * properties may use cardinality constraints
   * these constraints must be application-independent (use constraints in SHACL instead)
   * note that cardinality is OWL may not work as expected: it is used in reasoning, not in enforcing (use cardinality in SHACL instead).

## Reuse of ontologies, classes and properties
 * Reusing existing ontologies instead of creating a new one is highly encouraged, a list of existing ontologies can be found on [LOV](https://lov.okfn.org)
 * Reusing properties in different classes is encouraged (properties are not tied to a specific class)
 * Existing ontologies may be reused in part (only some classes, properties) or as a whole
 * It is not permitted to change/redefine existing properties (e.g. change rdfs:Domain or rdfs:Range), create a rdfs:subPropertyOf instead
 * It is not permitted to change/redefine existing classes, create a rdfs:subClassOf instead
 * Reused properties and classes that are not subclasses/subproperties should not be defined in the new ontology (must be part of SHACL validation instead)

## Example of reasoning vs enforcing cardinality

Although cardinality is often used in OWL to document constraints, it may have unexpected side-effects.
OWL was designed for reasoning (making assumptions about things that are not explicitly mentioned in the data)

E.g. 
  * if there is a constraint that an instance of type "Phone" can only have one "owner" of type "Person"
  * and the data mentions that persons "Mike" and "Anna" are both the owner of a specific phone 
  * and there is no additional info about "Mike" and "Anna"
  * a reasoner can conclude that "Mike" is the same person as "Anna"

## Example of properties not belonging to classes

In a similar way, properties don't belong to specific classes (unlike most OO-programming languages),
but a reasoner can use domains and ranges to make assumptions about the classes.

E.g. 
  * if there is a property "hasAuthor" with domain "Book" and range "Person"
  * and there "Hamlet" "hasAuthor" "Shakespeare"
  * and there is not additional info
  * a reasoner can conclude that "Hamlet" is a book and "Shakespeare" a Person
