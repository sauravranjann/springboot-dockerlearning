# 🚀 Spring Boot Microservices with Docker, PostgreSQL & Nginx

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-brightgreen?logo=springboot&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-9.7.1-blue?logo=gradle&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-29.2-2496ED?logo=docker&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?logo=postgresql&logoColor=white)
![Nginx](https://img.shields.io/badge/Nginx-Reverse%20Proxy-009639?logo=nginx&logoColor=white)

A complete, production-grade microservices architecture demonstrating containerization, inter-service communication, persistent database storage, and reverse proxy routing.

---

## 🏛️ System Architecture

```
                        [ Public Internet / Browser ]
                                     │
                                     ▼ (Port 80)
┌────────────────────────────────────────────────────────────────────────┐
│                        DOCKER COMPOSE NETWORK                          │
│                                                                        │
│   ┌────────────────────────────────────────────────────────────────┐   │
│   │                     NGINX Reverse Proxy                        │   │
│   │              (Only service exposed to Port 80)                 │   │
│   └────────────────┬──────────────────────────────┬────────────────┘   │
│                    │                              │                    │
│      Route /docker1/ (Internal: 8081)             │ Route /docker2/ (8082)
│                    ▼                              ▼                    │
│   ┌─────────────────────────────────┐   ┌──────────────────────────┐   │
│   │      dockerproject1-container   │   │ dockerproject2-container │   │
│   │     (Spring Boot + Spring Data) │ ◀─┤ (Spring Boot RestClient) │   │
│   └────────────────┬────────────────┘   └──────────────────────────┘   │
│                    │  Inter-service call:                              │
│                    │  http://dockerproject1-container:8081/db-test     │
│                    ▼ (Internal: 5432)                                  │
│   ┌─────────────────────────────────┐                                  │
│   │       postgres-db Container     │                                  │
│   │        (PostgreSQL 16 Alpine)   │                                  │
│   └────────────────┬────────────────┘                                  │
│                    │                                                   │
└────────────────────┼───────────────────────────────────────────────────┘
                     ▼
             [ Named Volume: postgres_data ]
             (Persistent data survives container restarts)
```

---

## 📦 Services Breakdown

| Service | Technology | Internal Port | Description |
| :--- | :--- | :--- | :--- |
| **`nginx-proxy`** | Nginx Alpine | `80` (Public) | Reverse proxy & API Gateway, handles public traffic |
| **`dockerproject1`** | Spring Boot 4 + JPA | `8081` (Private) | Core data service; creates schema and persists logs to PostgreSQL |
| **`dockerproject2`** | Spring Boot 4 + RestClient | `8082` (Private) | Microservice caller; queries Project 1 via Docker internal DNS |
| **`postgres-db`** | PostgreSQL 16 | `5432` (Private) | Relational database backed by persistent Docker volume |

---

## 🌐 API Endpoints Reference

| Endpoint | Routing Flow | Response |
| :--- | :--- | :--- |
| `GET /docker1/` | Nginx ➔ Project 1 | `docker project one 1` |
| `GET /docker1/db-test` | Nginx ➔ Project 1 ➔ PostgreSQL | Inserts a new record and returns all log entries as JSON |
| `GET /docker2/` | Nginx ➔ Project 2 | `docker project 2` |
| `GET /docker2/fetch-project1-data` | Nginx ➔ Project 2 ➔ Project 1 ➔ PostgreSQL | Project 2 queries Project 1 internally and returns the database records |

---

## ⚡ Quickstart Guide

### 1. Prerequisites
- Docker & Docker Compose (`docker --version`, `docker compose version`)
- JDK 21 (for local builds)

### 2. Launch Entire Stack with One Command
```bash
docker compose up -d --build
```

### 3. Verify Containers
```bash
docker compose ps
```

You should see:
```text
NAME                        IMAGE                   STATUS                  PORTS
nginx-proxy                 nginx:alpine            Up                      0.0.0.0:80->80/tcp
postgres-db                 postgres:16-alpine      Up (healthy)            5432/tcp
dockerproject1-container    dockerproject1:latest   Up                      8081/tcp
dockerproject2-container    dockerproject2:latest   Up                      8082/tcp
```

### 4. Test Live Endpoints
```bash
# Test Project 1
curl http://localhost/docker1/

# Test Database Persistence
curl http://localhost/docker1/db-test

# Test Microservice-to-Microservice REST Call
curl http://localhost/docker2/fetch-project1-data
```

---

## 🧠 Key Concepts & Learning Highlights

### 1. Docker Internal DNS
Containers on custom bridge networks do not use unstable IP addresses. They communicate directly using container names:
```properties
# Project 2 application.properties:
project1.url=${PROJECT1_URL:http://dockerproject1-container:8081}

# Project 1 application.properties:
spring.datasource.url=${DB_URL:jdbc:postgresql://postgres-db:5432/mydb}
```

### 2. Nginx Trailing Slash URL Rewriting
To prevent 404 errors with Spring Boot root mappings, Nginx strips the path prefix using trailing slashes:
```nginx
location /docker1/ {
    proxy_pass http://dockerproject1-container:8081/;
}
```

### 3. PostgreSQL 15/16 Permission Hardening
In PostgreSQL 15+, non-admin users must be granted schema ownership to execute DDL (`create table`):
```sql
ALTER SCHEMA public OWNER TO myuser;
GRANT ALL ON SCHEMA public TO myuser;
```

### 4. Database-per-Service Architecture
Project 2 never directly accesses Project 1's database. Instead, Project 2 uses modern Spring 6 `RestClient` to call Project 1's REST API over the secure internal Docker network.

---

## 🛠️ Handy Docker Cheatsheet

```bash
# View combined live logs of all services
docker compose logs -f

# View logs for a single service
docker compose logs -f dockerproject1

# Rebuild and restart after source code changes
docker compose up -d --build

# Stop the entire stack
docker compose down

# Stop and wipe database volume (clean reset)
docker compose down -v
```

---

## 👤 Author

**Saurav Ranjan**  
- Email: [sauravranjann@gmail.com](mailto:sauravranjann@gmail.com)  
- GitHub: [@sauravranjann](https://github.com/sauravranjann)
