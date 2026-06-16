# Toss Shopping

토스 쇼핑 FEP API 연동 서버

## 기술 스택

| 항목 | 내용 |
|---|---|
| Java | 17 |
| Spring Boot | 4.1.0 |
| HTTP Client | OkHttp 4.12.0 |
| JSON | Gson 2.11.0 |
| API 문서 | springdoc-openapi 2.8.9 (Swagger UI) |
| Build | Gradle 8.14 |

## 환경변수 설정

### Windows (PowerShell)
```powershell
$env:TOSS_CLIENT_ID="발급받은_access_key"
$env:TOSS_CLIENT_SECRET="발급받은_secret_key"
./gradlew bootRun
```

### Linux / macOS
```bash
export TOSS_CLIENT_ID=발급받은_access_key
export TOSS_CLIENT_SECRET=발급받은_secret_key
./gradlew bootRun
```

## API 목록

| 메서드 | 경로 | 설명 |
|---|---|---|
| GET | `/api/auth/token` | 액세스 토큰 발급 |
| GET | `/api/products?nextToken=` | 상품 목록 조회 |
| DELETE | `/api/products/{id}?partnerName=` | 상품 삭제 |
| DELETE | `/api/products/all?partnerName=` | 전체 상품 삭제 |

## Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

## 프로젝트 구조

```
src/main/java/com/toss/shopping/
├── TossShoppingApplication.java        # Spring Boot 진입점
│
├── config/
│   └── OkHttpConfig.java               # OkHttpClient Bean 설정
│
├── auth/
│   ├── TossAuthClient.java             # 토스 OAuth2 토큰 발급 (client_credentials)
│   └── TossTokenResponse.java          # 토큰 응답 DTO
│
├── product/
│   ├── TossProductClient.java          # 상품 API 호출 서비스
│   ├── TossProduct.java                # 상품 DTO
│   ├── TossProductResponse.java        # 상품 목록 응답 DTO
│   ├── TossProductSuccess.java         # 상품 목록 success 필드 DTO
│   ├── TossProductRemoveRequest.java   # 상품 삭제 요청 DTO
│   ├── TossProductRemoveResponse.java  # 상품 삭제 응답 DTO
│   └── TossProductRemoveAllResponse.java # 전체 삭제 결과 DTO
│
├── controller/
│   ├── TossAuthController.java         # 인증 REST Controller
│   └── TossProductController.java      # 상품 REST Controller
│
└── common/
    └── TossApiError.java               # 공통 에러 DTO
```

## 인증 방식

토스 OAuth2 `client_credentials` 방식으로 토큰을 발급받아 Bearer 토큰으로 API를 호출합니다.

```
POST https://oauth2.cert.toss.im/token
  grant_type=client_credentials
  scope=toss-shopping-fep:write
```

## 전체 상품 삭제 동작

`DELETE /api/products/all` 호출 시 페이지네이션(`hasNext`)을 순회하며 전체 상품을 삭제합니다.

```json
{
  "successCount": 10,
  "failCount": 0,
  "totalCount": 10
}
```
