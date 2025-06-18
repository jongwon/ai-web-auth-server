# Auth Server

Spring Boot 기반 인증 서버입니다.

## 기능

- 이메일/비밀번호 인증
- 카카오 OAuth2 로그인
- JWT 토큰 기반 인증
- Swagger UI 문서

## 실행 방법

### 1. PostgreSQL 시작
```bash
cd ../docker
docker-compose up -d
```

### 2. 환경 변수 설정
카카오 OAuth를 사용하려면 다음 환경 변수를 설정하세요:
- `KAKAO_CLIENT_ID`: 카카오 앱 REST API 키
- `KAKAO_CLIENT_SECRET`: 카카오 앱 Client Secret

### 3. 애플리케이션 실행
```bash
./gradlew bootRun
```

또는 IDE에서 `AuthApplication.java` 실행

## API 문서

http://localhost:15201/swagger-ui.html

## 주요 엔드포인트

### 인증
- POST `/api/auth/register` - 회원가입
- POST `/api/auth/login` - 로그인
- GET `/api/auth/me` - 현재 사용자 정보
- GET `/api/auth/oauth2/kakao` - 카카오 로그인

## 카카오 OAuth 설정

1. [Kakao Developers](https://developers.kakao.com)에서 애플리케이션 생성
2. 플랫폼 > Web에서 사이트 도메인 추가: `http://localhost:15201`
3. 카카오 로그인 > Redirect URI 추가: `http://localhost:15201/login/oauth2/code/kakao`
4. 동의항목에서 이메일, 프로필 정보 설정
5. 앱 키에서 REST API 키와 Client Secret 확인