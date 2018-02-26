# Dropwizard  vocab.belgif.be 
FROM openjdk:8-jre-alpine
MAINTAINER Bart Hanssens <bart.hanssens@bosa.fgov.be>

RUN adduser -S dropwizard 
COPY lod-vocab-1.2.1.jar /usr/local/bin/lod-vocab.jar
COPY vocab /home/dropwizard/ 
RUN chown -R dropwizard /home/dropwizard

USER dropwizard 
WORKDIR /home/dropwizard

ENV JAVA_OPT=""
ENV DW_CFG=""

CMD java -server ${JAVA_OPT} -jar /usr/local/bin/lod-vocab.jar server ${DW_CFG}

EXPOSE 8080 8081
 
