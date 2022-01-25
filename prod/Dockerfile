# syntax=docker/dockerfile:1

# start with downloading / converting data and compiling latest sources

FROM maven:3-openjdk-17-slim as builder
MAINTAINER bart.hanssens@bosa.fgov.be

RUN apt-get update && \
  apt-get install -y git-core && \
  useradd -m belgif

USER belgif

RUN cd /home/best && \
  git clone https://github.com/belgif/vocab-belgif && \
  cd /home/belgif/vocab-belgif && \
  mvn clean install -DskipTests 

# Dropwizard  vocab.belgif.be 

FROM eclipse-temurin:17-jre-alpine
MAINTAINER Bart Hanssens <bart.hanssens@bosa.fgov.be>

RUN adduser -S dropwizard 
COPY --from=0 /home/belgif/vocab-belgif/target/lod-vocab-*.jar /usr/local/bin/vocab-belgif.jar

COPY --from=0 /home/belgif/vocab-belgif/prod/cfg /home/dropwizard/ 

COPY --from=0 /home/belgif/vocab-belgif/prod/data/context /home/dropwizard/download-ctx/

COPY --from=0 /home/belgif/vocab-belgif/prod/data/shacl /home/dropwizard/import-shacl/
COPY --from=0 /home/belgif/vocab-belgif/prod/data/shacl /home/dropwizard/download-shacl/

COPY --from=0 /home/belgif/vocab-belgif/prod/data/ontologies /home/dropwizard/import-onto/
COPY --from=0 /home/belgif/vocab-belgif/prod/data/codelist /home/dropwizard/import-auth/

COPY --from=0 /home/belgif/vocab-belgif/prod/data/xmlns /home/dropwizard/download-ns/

RUN mkdir /home/dropwizard/download-auth  && \ 
    chown -R dropwizard:0 /home/dropwizard && \
    chmod -R g=u /home/dropwizard

USER dropwizard 
WORKDIR /home/dropwizard

ENV JAVA_OPT=""
ENV DW_CFG="config.yml"

CMD java -server ${JAVA_OPT} -jar /usr/local/bin/vocab-belgif.jar server ${DW_CFG}

EXPOSE 8080 8081
 
