# ================== STAGE 1: Build jar ==================
# Sử dụng image Maven có sẵn JDK 17 để build source code
FROM maven:3.8.4-openjdk-17 AS build

# Đặt thư mục làm việc trong container
WORKDIR /app

# Copy file pom.xml vào container để Maven tải dependency trước
COPY pom.xml .

# Copy thư mục src (chứa code Java) vào container
COPY src ./src

# Build ứng dụng, bỏ qua test (để nhanh hơn)
RUN mvn clean package -DskipTests


# ================== STAGE 2: Run jar ==================
# Sử dụng image JDK 17 nhẹ hơn để chạy ứng dụng
FROM openjdk:17-jdk

# Đặt thư mục làm việc
WORKDIR /app

# Copy file jar đã build từ stage 1 sang stage 2
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 (nơi Spring Boot chạy)
EXPOSE 8080

# Lệnh khởi chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
