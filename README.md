# backend


## 기술 스택

### 개발

- Java 17
- Spring Boot
- Spring Security
- JPA (Java Persistence API)
- OAuth 2.0
- QueryDSL
- JWT (JSON Web Token)

### DB
- Redis
- PostgreSQL

### DevOps

- AWS EC2
- AWS S3

### 프로젝트 관리

- Jira

### 커뮤니티 및 소통

- Discord

## 전체 구성도
![image](https://github.com/user-attachments/assets/53d1023a-dbf4-439c-8e13-8b62f7f4abdc)


## ERD
![image](https://github.com/user-attachments/assets/171359f3-f848-4c7b-afe3-94966e08b3af)

## 설치 및 실행방법

- application.yml
```
Server:
  tomcat:
    max-http-form-post-size: 
jwt:
  secret:
spring:
  datasource:
    url: 
    username: 
    password: 
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: create # 운영 시, none으로 표기
    database: postgresql
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    open-in-view: true
#    show-sql: true
    generate-ddl: true
    properties:
      hibernate:
        default_batch_fetch_size: 100
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: 
            client-secret: 
            redirect-uri: 
            authorization-grant-type: authorization_code
            scope: profile, email

          kakao:
            client-id: 
#            redirect-uri: 
            redirect-uri: 
            client-authentication-method: client_secret_post
            authorization-grant-type: authorization_code
            scope: profile_nickname, account_email

        provider:
          kakao:
            authorization_uri: https://kauth.kakao.com/oauth/authorize
            token_uri: https://kauth.kakao.com/oauth/token
            user-info-uri: https://kapi.kakao.com/v2/user/me
            user_name_attribute: id
  aws:
    accessKeyId: 
    secretAccessKey: 
    region: 
    s3:
      bucketName: 
  data:
    redis:
      host: 
      port: 

logging.level:
  org.hibernate.SQL: debug
```

- 로컬에서 Docker로 서버 실행하기
  - PostgreSQL, Redis는 로컬 피시에서 실행되고 있다는 가정하에 실행 합니다.
```
docker compose up -d .
```
