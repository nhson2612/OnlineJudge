# SIDE PROJECT - Hands-on practice for learning Java, Docker, Spring Boot, Git and more

# Chấm điểm bài thi lập trình tự động

- ✅ Biên dịch & chạy chương trình trong môi trường Docker
- ✅ So khớp đầu ra với test case
- ✅ Tính điểm dựa trên số test case đúng
- ✅ Ghi log và báo lỗi chi tiết nếu có
- ✅ Phát hiện bài làm có dấu hiệu **sao chép (plagiarism)** nếu bật tính năng

---
## 🏗️ Kiến Trúc Tổng Quan
[Kiểm tra tại đây]()

## 🚀 Tính Năng Chính

- ✅ Giao diện API để nộp bài (**in progress**)
- ✅ Dùng Docker để biên dịch & chạy code an toàn (complete v1.0)
- ✅ Hỗ trợ nhiều ngôn ngữ (C++, Java, Python…) (complete v1.0)
- ✅ So sánh output từng test case với file mẫu (complete v1.0)
- ✅ Tính điểm tự động và lưu kết quả (**in progress**)
- ✅ Phát hiện gian lận bằng so sánh mã nguồn (complete v1.0)
- ✅ Tự động retry nếu chấm lỗi (3 lần) (**in progress**)



---

## 🧰 Công Nghệ Sử Dụng

| Thành phần              | Công nghệ                       |
|-------------------------|---------------------------------|
| Backend                 | Java 21 + Spring Boot           |
| Chấm code               | Docker + Shell Scripts          |
| AST & Token analysis    | ANTLR/JavaParser (**expected**) |
| Queue xử lý song song   | BlockingQueue + ExecutorService |
| Build tool              | Maven                           |
| DB                      | MySQL                           |

---

## ⚙️ Hướng Dẫn Cài Đặt

### 1. Yêu cầu

- Java 21 hoặc các phiên bản LTS khác
- Docker
- Maven
- Git

### 2. Clone và build

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

### 4. Chạy ứng dụng

```bash
java -jar target/judge-system.jar
```

# 👨‍💻Tác giả
- Tác giả chính : **Nguyễn Huy Sơn** - Student
- Hỗ trợ :
  > - **ChatGpt 🤖🤖🤖🤖🤖**
  > - **Claude 🤖🤖🤖🤖🤖**
  > - **Deepseek 🤖🤖🤖🤖🤖**
  > - **Grok 🤖🤖🤖🤖**
  > - **Qwen 🤖🤖🤖**
  > - **Gemini 🤖🤖🤖**

# UI IS IN DEVELOPING AND WILL BE INTRODUCED SOON ,HOPE YOU ENJOY THIS PROJECT!
