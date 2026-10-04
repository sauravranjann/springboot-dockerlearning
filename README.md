# Spring Boot (Gradle) Docker Learning Projects

This setup contains two independent Spring Boot projects built using **Gradle** to help you learn and practice Docker.

---

## 1. Project Overview

| Project | Port | JAR Output & Endpoint Message | JAR Location (Gradle) |
| :--- | :--- | :--- | :--- |
| **dockerproject1** | `8081` | `docker project one 1` | `d:\data\dockerproject1\build\libs\dockerproject1-0.0.1-SNAPSHOT.jar` |
| **dockerproject2** | `8082` | `docker project 2` | `d:\data\dockerproject2\build\libs\dockerproject2-0.0.1-SNAPSHOT.jar` |

---

## 2. Running the JAR Files Directly

### Run Project 1:
```powershell
cd d:\data\dockerproject1
java -jar build\libs\dockerproject1-0.0.1-SNAPSHOT.jar
```
- Console Output:
  ```text
  ==================================================
  docker project one 1
  ==================================================
  ```
- Browser / curl test: `http://localhost:8081/` -> `docker project one 1`

---

### Run Project 2:
```powershell
cd d:\data\dockerproject2
java -jar build\libs\dockerproject2-0.0.1-SNAPSHOT.jar
```
- Console Output:
  ```text
  ==================================================
  docker project 2
  ==================================================
  ```
- Browser / curl test: `http://localhost:8082/` -> `docker project 2`

---

## 3. Rebuilding the JAR Files with Gradle

If you modify source code in either project, rebuild with the Gradle wrapper:
```powershell
# In dockerproject1:
cd d:\data\dockerproject1
.\gradlew.bat bootJar

# In dockerproject2:
cd d:\data\dockerproject2
.\gradlew.bat bootJar
```

---

## 4. Docker Guide for Beginners

### Understanding `WORKDIR /app` in the Dockerfile
```dockerfile
# 1. Base Image: Eclipse Temurin OpenJDK 21
FROM eclipse-temurin:21-jre-alpine

# 2. Sets the working directory inside the container
WORKDIR /app

# 3. Copies the jar file into /app/app.jar
COPY build/libs/*.jar app.jar

# 4. Exposes the port
EXPOSE 8081

# 5. Executes java -jar app.jar inside /app
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### What does `WORKDIR /app` do?
1. **Creates and changes directory**: It functions just like running `mkdir -p /app && cd /app` inside the container.
2. **Relative paths resolve here**: When we run `COPY build/libs/*.jar app.jar`, `app.jar` is placed at `/app/app.jar`.
3. **Execution context**: When `ENTRYPOINT ["java", "-jar", "app.jar"]` runs, it executes inside `/app`.
4. **Best practice**: Without `WORKDIR`, everything would land in the Linux root directory (`/`), which is considered bad practice.

---

### Building and Running Containers

#### Build Docker Images:
```powershell
# Project 1:
cd d:\data\dockerproject1
docker build -t dockerproject1:latest .

# Project 2:
cd d:\data\dockerproject2
docker build -t dockerproject2:latest .
```

#### Run Containers:
```powershell
# Project 1:
docker run -d --name project1-container -p 8081:8081 dockerproject1:latest

# Project 2:
docker run -d --name project2-container -p 8082:8082 dockerproject2:latest
```

#### Run Both with Docker Compose:
From `d:\data`:
```powershell
docker compose up -d --build
```
