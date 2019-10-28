# Dropwizard  vocab.belgif.be 
FROM amd64/openjdk:8-jre-alpine
MAINTAINER Bart Hanssens <bart.hanssens@bosa.fgov.be>

RUN adduser -S dropwizard 
COPY lod-vocab-1.3.12.jar /usr/local/bin/lod-vocab.jar

COPY vocab /home/dropwizard/ 

COPY lod-vocab-data/context /home/dropwizard/download-ctx/

COPY lod-vocab-data/shacl /home/dropwizard/import-shacl/
COPY lod-vocab-data/shacl /home/dropwizard/download-shacl/

COPY lod-vocab-data/ontologies /home/dropwizard/import-onto/
COPY lod-vocab-data/codelist /home/dropwizard/import-auth/

COPY lod-vocab-data/xmlns /home/dropwizard/download-ns/

RUN mkdir /home/dropwizard/download-auth  && \ 
    chown -R dropwizard:0 /home/dropwizard && \
    chmod -R g=u /home/dropwizard

USER dropwizard 
WORKDIR /home/dropwizard

ENV JAVA_OPT=""
ENV DW_CFG=""

CMD java -server ${JAVA_OPT} -jar /usr/local/bin/lod-vocab.jar server ${DW_CFG}

EXPOSE 8080 8081
 
