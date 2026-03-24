# Usar uma imagem base do OpenJDK
FROM openjdk:17-jdk-alpine

# Criar diretório de trabalho dentro do container
WORKDIR /app

# Copiar o JAR gerado pelo Maven para dentro do container
COPY target/*.jar app.jar

# Expor a porta padrão do Spring Boot
EXPOSE 8080

# Comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]
