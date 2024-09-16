# Challenge3 App

Este projeto é um WebApp Spring Boot usando MVC com Thymeleaf para gerenciamento de empresas. Ele foi containerizado usando Docker e está sendo executado no Azure.

## Vídeo Youtube
[Video Demonstração Spring on Azure](https://youtu.be/WgJezOZBIFg)


## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.2.8**
  - Spring Data JPA
  - Thymeleaf
  - Spring Web
  - Validação
- **H2 Database** (para desenvolvimento)
- **Lombok**
- **Oracle JDBC (ojdbc8)**
- **Maven** (para gerenciamento de dependências e build)
- **Docker** (para containerização)
- **Azure** (para deploy)

## Configuração do Projeto

### Pré-requisitos

- Java 17
- Maven
- Docker
- Conta no Docker Hub
- Conta no Azure

### Passos para Build e Deploy

1. **Clone o repositório:**

````bash
git clone https://github.com/leoyuuki/challenge3-app.git
cd challenge3-app
````

2. **Build do projeto com Maven:**

   Execute o comando abaixo para compilar o projeto e gerar o arquivo `.jar`:

````bash
mvn clean package
````

3. **Criação da Imagem Docker:**

   Com o JAR gerado, crie a imagem Docker:

````bash
docker build -t leoyuuki/challenge3-app:v1 .
````

4. **Envio da Imagem para o Docker Hub:**

   Faça login no Docker Hub e envie a imagem:

````bash
docker login
docker push leoyuuki/challenge3-app:v1
````

5. **Deploy no Azure:**

   - Crie um Web App no Azure, selecionando a opção **Docker Container**.
   - Na aba **Docker**, configure para usar a imagem `leoyuuki/challenge3-app:v1` do Docker Hub.

6. **Verificar Log Stream:**

   Após o deploy, você pode monitorar o status do aplicativo pelo Log Stream no portal da Azure.

## Contribuição

Se desejar contribuir com o projeto, siga os passos:

1. Faça um fork do repositório
2. Crie uma nova branch: `git checkout -b minha-feature`
3. Commit suas mudanças: `git commit -m 'Adicionando nova feature'`
4. Faça o push da branch: `git push origin minha-feature`
5. Abra um pull request


