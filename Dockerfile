# Sử dụng image chứa Java 17
FROM openjdk:17-oracle
ENV TZ=Asia/Ho_Chi_Minh
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép các tệp cần thiết của ứng dụng Spring vào thư mục làm việc
COPY target/MyNote-0.0.1-SNAPSHOT.jar MyNote-0.0.1-SNAPSHOT.jar

# Mở cổng 8080 để ứng dụng có thể truy cập
EXPOSE 8080

# Chạy ứng dụng Spring
CMD ["java", "-jar", "MyNote-0.0.1-SNAPSHOT.jar"]
