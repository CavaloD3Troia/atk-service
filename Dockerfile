# The jar is built on the host first (`make package`), so building the image needs no Maven
# and no GitHub Packages credentials.
FROM eclipse-temurin:25-jre

RUN groupadd --system atk && useradd --system --gid atk atk

WORKDIR /app
COPY application/target/atk-service-exec.jar app.jar

USER atk
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
