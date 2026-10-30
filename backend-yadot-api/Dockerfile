# ESTÁGIO 1: Build (Compilação)
# Nomeamos esta etapa como "builder" para referenciá-la depois
FROM maven:3.9-eclipse-temurin-25 AS builder

WORKDIR /app

# Copia apenas o pom.xml e baixa as dependências (Cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte e compila o .jar final
COPY src ./src
RUN mvn clean package -DskipTests

# ESTÁGIO 2: Run (Execução)
# Imagem leve apenas com o ambiente de execução (JRE)
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# Pega o .jar gerado na etapa "builder" e copia para cá com o nome "app.jar"
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta que o Spring Boot/Java usa por padrão
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]