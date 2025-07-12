
# SIDE PROJECT : Hands-on practice for learning **Java**, **Docker**, **Git** and more!

# Automatically Grading System

  - ✅ Compile and run code safely using Docker
  - ✅ Match output of each test case with expected results
  - ✅ Automatically grade code submissions
  - ✅ Export results as JSON, CSV and more formats (**expected v2.0**)
  - ✅ Detect plagiarism by comparing source code

---
## 🏗️ Overview Architecture
[Check it out]()

## 🚀 Core Features

  - ✅ UI and API for submitting code (**In progress**)
  - ✅ Using Docker to compile and run code safely (complete v1.0)
  - ✅ Support multiple programming languages: C++, Java, Python (**only three languages for now**)
  - ✅ Compare output of each testcase with expected output (**complete v1.0**)
  - ✅ Automatically grade code submissions (complete v1.0)
  - ✅ Detect plagiarism by comparing source code (complete v1.0)
  - ✅ Automatically retry failed test cases (**In progress**)



---

## 🧰 Tech stack

| Components           |                                 |
|----------------------|---------------------------------|
| Backend              | Java 21 + Spring Boot           |
| Grade                | Docker + Shell Scripts          |
| AST & Token analysis | ANTLR/JavaParser (**dự kiến**)  |
| Build tool           | Maven                           |
| DB                   | MySQL                           |
| Others               | BlockingQueue + ExecutorService |

---

## ⚙️ Guide

### 1. Requirements

- Java 21 or other LTS version
  - Docker
  - Maven
  - Git

### 2. Clone and build

```bash
git clone https://github.com/your-org/judge-system.git
cd judge-system
mvn clean install
```

### 3. Build Docker image

```bash
cd docker/cpp
docker build -t judge-cpp .

cd ../java
docker build -t judge-java .

cd ../python
docker build -t judge-python .
```

### 4. Run the application

```bash
java -jar target/judge-system.jar
```

# 👨‍💻 Contributors
- **Nhson2612** - Student
  - Supported by:
  >   - **ChatGpt 🤖🤖🤖🤖🤖**
  >   - **Claude 🤖🤖🤖🤖🤖**
  >   - **Deepseek 🤖🤖🤖🤖🤖**
  >   - **Grok 🤖🤖🤖🤖**
  >   - **Qwen 🤖🤖🤖**
  >   - **Gemini 🤖🤖🤖**
  >   - **Llama 3 🤖🤖🤖**

# UI is under development and will be introduced soon, hope you like this project!