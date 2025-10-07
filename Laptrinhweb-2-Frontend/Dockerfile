# ================== STAGE 1: Build React app ==================
FROM node:18 AS build

# Đặt thư mục làm việc trong container
WORKDIR /app

# Copy file khai báo dependencies
COPY package*.json ./

# Cài đặt dependencies (node_modules)
RUN npm install

# Copy toàn bộ source code vào container
COPY . .

# Build ứng dụng React thành static files (output nằm trong /build)
RUN npm run build


# ================== STAGE 2: Serve React app ==================
# 👉 Có 2 lựa chọn: DEV (npm start) hoặc PROD (Nginx)

# ---------------- DEV mode (npm start) ----------------
# Chỉ dùng khi phát triển (hot reload)
EXPOSE 4000
CMD ["npm", "run", "dev"]

# ---------------- PROD mode (Nginx) ----------------
# Uncomment nếu bạn muốn build production
# FROM nginx:1.25.1
# COPY --from=build /app/build /usr/share/nginx/html
# EXPOSE 80
# CMD ["nginx", "-g", "daemon off;"]
