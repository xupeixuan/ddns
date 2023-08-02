FROM gradle:7.6-jdk17 AS builder
WORKDIR /app
COPY ./ ./
RUN gradle clean bootJar

FROM openjdk:17-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/ddns-*.jar /app/ddns.jar

CMD ["/bin/sh", "-c", "java -Dspring.profiles.active=prod -Dalidns.accessKeyId=$(cat $ALIDNS_ACCESS_KEY_FILE)   \
                            -Dalidns.secret=$(cat $ALIDNS_SECRET_FILE) \
                            -Dalidns.dns.domain=$ALIDNS_DOMAIN \
                            -Dalidns.dns.subDomains=$ALIDNS_SUB_DOMAINS \
                            -Dalidns.dns.type=$ALIDNS_TYPE \
                            -jar ddns.jar"]