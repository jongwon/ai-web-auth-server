# 카카오 OAuth 설정 가이드

## 1. Kakao Developers 설정

### 애플리케이션 생성
1. [Kakao Developers](https://developers.kakao.com) 접속
2. 내 애플리케이션 > 애플리케이션 추가하기
3. 앱 이름, 사업자명 입력

### 플랫폼 설정
1. 앱 설정 > 플랫폼 > Web 플랫폼 등록
2. 사이트 도메인: `http://localhost:15201`

### 카카오 로그인 설정
1. 제품 설정 > 카카오 로그인 > 활성화
2. Redirect URI 등록: `http://localhost:15201/login/oauth2/code/kakao`

### 동의항목 설정
1. 제품 설정 > 카카오 로그인 > 동의항목
2. 다음 항목 설정:
   - 닉네임: 필수 동의
   - 카카오계정(이메일): 필수 동의

### 보안 설정
1. 앱 설정 > 보안
2. Client Secret 생성 및 복사

### 앱 키 확인
1. 앱 설정 > 앱 키
2. REST API 키 복사

## 2. 애플리케이션 환경 변수 설정

### IntelliJ IDEA
1. Run/Debug Configurations
2. Environment variables에 추가:
   ```
   KAKAO_CLIENT_ID=your-rest-api-key
   KAKAO_CLIENT_SECRET=your-client-secret
   ```

### 터미널에서 실행
```bash
export KAKAO_CLIENT_ID=your-rest-api-key
export KAKAO_CLIENT_SECRET=your-client-secret
./gradlew bootRun
```

## 3. 테스트 방법

### PostgreSQL 시작
```bash
cd ../docker
docker-compose up -d
```

### 애플리케이션 실행
```bash
./gradlew bootRun
```

### Swagger UI 접속
http://localhost:15201/swagger-ui.html

### 카카오 로그인 테스트
1. 브라우저에서 접속: http://localhost:15201/api/auth/oauth2/kakao
2. 카카오 로그인 페이지로 리다이렉트됨
3. 카카오 계정으로 로그인
4. 동의 후 완료되면 JWT 토큰과 함께 리다이렉트됨

### 회원가입/로그인 테스트 (Swagger UI)
1. POST /api/auth/register로 회원가입
2. POST /api/auth/login으로 로그인
3. 받은 JWT 토큰을 Authorize 버튼에 입력
4. GET /api/auth/me로 사용자 정보 조회

## 문제 해결

### 인증 실패 시
- 환경 변수가 제대로 설정되었는지 확인
- Redirect URI가 정확히 일치하는지 확인
- 동의항목이 올바르게 설정되었는지 확인

### 로그 확인
```bash
# application.yml에서 로그 레벨이 DEBUG로 설정되어 있음
tail -f logs/spring.log
```