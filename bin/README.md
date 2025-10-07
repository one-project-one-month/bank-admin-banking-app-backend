# Sample Java gRPC Microservices

This project demonstrates a simple **gRPC microservices architecture** using Java, Spring Boot, Kafka, and Docker.

## 📂 Project Structure

```

sample-java-grpc/
├── auth-service          # Authentication microservice
├── gateway-service       # API Gateway microservice
├── greeting-service      # Greeting microservice
├── infra                 # Infrastructure (Kafka, Zookeeper, Docker Compose)

````

---

## 🚀 Prerequisites

Make sure you have the following installed:

- [Java 17+](https://adoptium.net/)
- [Maven](https://maven.apache.org/)
- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/)

---

## ⚡️ Build Services

From the root of the project, run:

```bash
# Build auth-service
cd auth-service
mvn clean install
cd ..

# Build gateway-service
cd gateway-service
mvn clean install
cd ..

# Build greeting-service
cd greeting-service
mvn clean install
cd ..
````

---

## 🐳 Run with Docker Compose

Start infrastructure and all services:

```bash
cd infra
sudo docker compose up --build -d
```

---

## 🔍 Verify Running Services

Check running containers:

```bash
docker ps
```

Logs for a specific service:

```bash
docker logs gateway-service -f
```

---

## 📡 Services & Ports

| Service          | Port |
| ---------------- | ---- |
| Zookeeper        | 2181 |
| Kafka            | 9092 |
| Auth Service     | 9090 |
| Greeting Service | 9091 |
| Gateway Service  | 8080 |

---

## 🛑 Stop Services

To stop everything:

```bash
cd infra
sudo docker compose down
```

---

## ✨ Notes

* The `infra/docker-compose.yml` file defines Kafka, Zookeeper, and the microservices.
* Update the healthchecks if needed for your environment.
* Future improvements: CI/CD pipeline, integration tests, and deployment configs.
