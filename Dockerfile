# Usar a imagem oficial do OpenJDK como base
FROM openjdk:17-jdk-slim

# Definir o diretório de trabalho no container
WORKDIR /app

# Copiar o arquivo JAR gerado pelo Maven para o diretório /app no container
COPY target/challenge3-0.0.1-SNAPSHOT.jar /app/challenge3.jar

# Expor a porta em que o Spring Boot está rodando (a porta padrão é 8080)
EXPOSE 8080

# Comando para rodar a aplicação Java dentro do container
ENTRYPOINT ["java", "-jar", "/app/challenge3.jar"]