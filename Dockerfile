# Sử dụng image chứa Java 17
FROM adoptopenjdk/openjdk17:jdk-17.0.0_35-alpine

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép các tệp cần thiết của ứng dụng Spring vào thư mục làm việc
COPY target/*.jar app.jar

# Mở cổng 8080 để ứng dụng có thể truy cập
EXPOSE 8080

# Chạy ứng dụng Spring
CMD ["java", "-jar", "app.jar"]
