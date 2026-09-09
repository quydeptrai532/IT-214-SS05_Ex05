# Bài Tập 5 — A/B Testing với Weight Route Predicate (Spring Cloud Gateway)

**Mã bài toán:** SPRING-CLOUD-S05-EX05

## Mục tiêu
Điều hướng 80% lưu lượng search về `search-service-v1` (stable) và 20% về `search-service-v2` (experimental) — **hoàn toàn bằng application.yml**, không code thêm class Java.

---

## 1. Cấu hình Gateway — `api-gateway/src/main/resources/application.yml`

```yaml
server:
  port: 8080

spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      routes:
        - id: search-service-v1
          uri: http://localhost:8081
          predicates:
            - Path=/api/search
            - Weight=SearchGroup, 8
        - id: search-service-v2
          uri: http://localhost:8082
          predicates:
            - Path=/api/search
            - Weight=SearchGroup, 2
```

## 2. Ứng dụng giả lập

| Ứng dụng | Port | Endpoint | Response |
|----------|------|----------|----------|
| `search-service-v1` | 8081 | `GET /api/search` | `V1` |
| `search-service-v2` | 8082 | `GET /api/search` | `V2` |

## 3. Giải thích cơ chế Weight hoạt động (3-5 dòng)

Khi request `/api/search` đến, Gateway kiểm tra các route trong cùng weight group **SearchGroup**. Do cả 2 route đều khớp Path predicate, Gateway chọn route dựa trên **xác suất tỷ lệ weight**: `P(v1) = 8/(8+2) = 80%`, `P(v2) = 2/(8+2) = 20%`. Với mỗi request, Gateway sinh một số ngẫu nhiên trong khoảng tổng weight (10) để quyết định route nào được chọn → số lượng request phân bổ xấp xỉ 80-20. Load Balancer (nếu có) sẽ tiếp tục chọn instance trong route được chọn.

---

## 4. Cách chạy & test

### 4.1. Khởi động 2 service giả lập
```bash
cd search-service-v1 && ./gradlew bootRun &
cd search-service-v2 && ./gradlew bootRun &
```

### 4.2. Khởi động Gateway
```bash
cd ../api-gateway && ./gradlew bootRun &
```

### 4.3. Gửi 100 request để kiểm chứng phân bổ

**Cách 1 — Bash / cURL (Linux/macOS):**
```bash
for i in $(seq 1 100); do curl -s http://localhost:8080/api/search; echo; done | sort | uniq -c
```

**Cách 2 — PowerShell (Windows):**
```powershell
$results = for ($i=1; $i -le 100; $i++) { (Invoke-WebRequest -Uri "http://localhost:8080/api/search").Content }
$v1 = ($results | Where-Object { $_ -eq "V1" }).Count
$v2 = ($results | Where-Object { $_ -eq "V2" }).Count
"V1 = $v1 / 100  |  V2 = $v2 / 100"
```

**Cách 3 — Postman Collection runner** hoặc câu lệnh:
```bash
ab -n 100 -c 1 http://localhost:8080/api/search
```
(hoặc dùng `curl` + `watch` số lần đếm V1/V2)

---

## 5. Bảng thống kê kết quả thực tế (điền sau khi chạy)

> Lý tưởng: 80 request → V1, 20 request → V2. Kết quả thực tế thường dao động nhẹ quanh tỷ lệ này.

| Lượt test | Tổng request | Số request → V1 | Số request → V2 | Tỷ lệ V1 | Tỷ lệ V2 |
|-----------|--------------|------------------|------------------|----------|----------|
| (vd) Test 1 | 100 | 78 | 22 | 78% | 22% |
| (vd) Test 2 | 100 | 83 | 17 | 83% | 17% |
| (vd) Test 3 | 100 | 79 | 21 | 79% | 21% |
| **Trung bình** | **300** | **240** | **60** | **~80%** | **~20%** |

## 6. Ghi chú quan trọng về Weight Predicate

- Các route trong **cùng 1 weight group** phải có **Path predicate giống hệt nhau** — chỉ khác nhau ở `Weight`.
- Nếu một route trong group không matches Path → Gateway bỏ qua weight của route đó và chia lại tỷ lệ cho các route còn lại.
- Weight giúp **black-box A/B testing**: thay đổi giao diện/API của service candidate không cần sửa Gateway — chỉ cần sửa tỷ lệ weight trong YAML.

## 7. File trong bài

```
Session05/Ex05/
├── api-gateway/
│   ├── build.gradle
│   ├── settings.gradle
│   └── src/main/
│       ├── java/com/vietmart/gateway/ApiGatewayApplication.java
│       └── resources/application.yml          ← Weight config (SearchGroup 8:2)
├── search-service-v1/                          ← trả "V1" (port 8081)
├── search-service-v2/                          ← trả "V2" (port 8082)
├── README.md
└── screenshots/                                ← ảnh kết quả test (tuỳ chọn)
```