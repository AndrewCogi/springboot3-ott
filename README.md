# Spring Boot 3 + Spring Security 6.4.1: One-Time Token (OTT) 인증 프로젝트

이 프로젝트는 이메일 기반의 One-Time Token(OTT) 인증 시스템을 구현한 Spring Boot 애플리케이션입니다. 사용자는 발급된 OTT를 통해 인증을 진행하며, 유효 시간 내 인증 절차를 완료해야 합니다.

---

## 주요 기능

1. **OTT 생성 및 전송**  
   사용자가 `/auth/ott/generate?username=[userEmail]`로 인증 링크를 요청하면 OTT가 생성되어 Redis에 저장되고, 이메일로 인증 링크가 전송됩니다.

2. **OTT 인증**  
   사용자는 이메일로 전송된 링크를 클릭하여 인증을 진행합니다. 인증 성공 시 Redis의 OTT 정보를 파기하고 인증 상태를 갱신합니다.

3. **앱 인증**
   사용자는 OTT 인증 후, 앱에서 [인증 완료] 버튼을 눌러 최종 로그인을 진행합니다. (Jwt 발급 등)
   
4. **유효 시간 관리**  
   - OTT 인증 링크 유효 시간: **5분**  
   - 인증 성공 후 앱에서 [인증 완료] 버튼을 누르기 전까지의 유효 시간: **3분**

---

## 전체 흐름

### 1. OTT 생성 요청
- **Endpoint**: `GET /auth/ott/generate?username=[userEmail]`  
- **설명**:  
  - 사용자 이메일로 전송할 인증 링크를 생성합니다.
  - Redis에 다음 정보를 저장:
    - `authStatus`: 사용자의 OTT 인증 여부 (`isVerify=false` 초기화)
    - `ott`: OTT 토큰 값
  - 두 키 모두 TTL(유효 기간)은 **5분**으로 설정됩니다.
  - 이메일에 인증 링크(`http://localhost:8080/auth/ott/login?token=[tokenValue]`)를 포함하여 사용자에게 전송합니다.

---

### 2. OTT 인증 요청
- **Endpoint**: `GET /auth/ott/login?token=[tokenValue]`  
- **설명**:  
  - 사용자가 이메일로 전송된 링크를 클릭하면 서버는 해당 GET 요청을 처리하여 POST 요청을 수행합니다.  
  - 내부적으로 `/auth/ott/verify?token=[tokenValue]`로 POST 요청을 전환합니다.

---

### 3. OTT 인증 검증 (직접 요청하지 않음)
- **Endpoint**: `POST /auth/ott/verify?token=[tokenValue]`  
- **설명**:  
  - OTT 검증 결과에 따라 성공/실패 응답을 반환합니다.  
  - **성공 시**:
    - Redis의 `ott` 정보 삭제.
    - `authStatus`의 `isVerify=true`로 설정 및 TTL을 **3분**으로 갱신.
  - **실패 시**:
    - Redis에 저장된 정보에 변화 없음.

---

## API 요약

| Endpoint                               | Method | 설명                                      |
|----------------------------------------|--------|------------------------------------------|
| `/auth/ott/generate?username=[userEmail]` | GET    | OTT 생성 및 사용자 이메일로 인증 링크 전송     |
| `/auth/ott/login?token=[tokenValue]`     | GET    | 사용자의 인증 요청 처리                      |
| `/auth/ott/verify?token=[tokenValue]`    | POST   | OTT 검증 및 인증 상태 갱신 (사용자 접근 x)     |

---

## Sequence Diagram

![spring-security-6 4 1-ott drawio](https://github.com/user-attachments/assets/b8943c85-a460-44dd-a96d-998e1a6d886f)

