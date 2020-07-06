# Dropwizard  vocab.belgif.be 
FROM adoptopenjdk/openjdk11:alpine-jre
MAINTAINER Bart Hanssens <bart.hanssens@bosa.fgov.be>

RUN adduser -S dropwizard 
COPY lod-vocab-1.4.0.jar /usr/local/bin/vocab-belgif.jar

COPY cfg /home/dropwizard/ 

COPY data/context /home/dropwizard/download-ctx/

COPY data/shacl /home/dropwizard/import-shacl/
COPY data/shacl /home/dropwizard/download-shacl/

COPY data/ontologies /home/dropwizard/import-onto/
COPY data/codelist /home/dropwizard/import-auth/

COPY data/xmlns /home/dropwizard/download-ns/

RUN mkdir /home/dropwizard/download-auth  && \ 
    chown -R dropwizard:0 /home/dropwizard && \
    chmod -R g=u /home/dropwizard

USER dropwizard 
WORKDIR /home/dropwizard

ENV JAVA_OPT=""
ENV DW_CFG="config.yml"

CMD java -server ${JAVA_OPT} -jar /usr/local/bin/vocab-belgif.jar server ${DW_CFG}

EXPOSE 8080 8081
 
