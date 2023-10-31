# Sử dụng hình ảnh cơ sở chứa Java 11
FROM openjdk:11-jre-slim

# Tạo thư mục /app trong hình ảnh và sử dụng nó làm thư mục làm việc
WORKDIR /app

# Sao chép tất cả các tệp JAR từ máy tính host vào thư mục /app trong hình ảnh
COPY build/libs/*.jar app.jar

# Thiết lập biến môi trường
ENV JAVA_OPTS=""

# Mở cổng 8080
EXPOSE 8080

# Khởi chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]

# Cấu hình biến môi trường
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://dpg-ckg4se8l3its73dig5pg-a.singapore-postgres.render.com:5432/aomanager
ENV SPRING_DATASOURCE_USERNAME=aomanager_user
ENV SPRING_DATASOURCE_PASSWORD=TEeNGWhSc3tgrAqaCfzyEiLtq6hWqNYp
