# SpringBoot-Security
스프링 시큐리티 인증, 권한 체크 완료 (JPA, mustache 적용), Oauth2(Google)

# 스프링 시큐리티 기본 V1

### MySQL DB 및 사용자 생성

```sql
create user 'cos'@'%' identified by 'cos1234';
GRANT ALL PRIVILEGES ON *.* TO 'cos'@'%';
create database security;
use security;
```

### SecurityConfig.java 권한 설정 방법

```java
// protected void configure(HttpSecurity http) 함수 내부에 권한 설정법
.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_USER')")
.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
```

### 컨트롤러의 함수에 직접 권한 설정 하는 방법

```java
// 특정 주소 접근시 권한 및 인증을 위한 어노테이션 활성화 SecurityConfig.java에 설정
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)

// 컨트롤러에 어노테이션 거는 법
@PostAuthorize("hasRole('ROLE_MANAGER')")
@PreAuthorize("hasRole('ROLE_MANAGER')")
@Secured("ROLE_MANAGER")
```

###Oauth2 구글 프로필 사용
```java
{sub=111061868356786625939, name=이서영, given_name=서영, 
family_name=이, 
picture=https://lh3.googleusercontent.com/a/ACg8ocISbEIK54hcznKJPedKPlnWJQCz3gneNxo_SwiFtqSi=s96-c, 
email=seoyeongi851@gmail.com,
 email_verified=true, locale=ko}


username = "google_111061868356786625939"
password = "암호화(겟인데어)"
email = "seoyeongi851@gmail.com"
role = "ROLE_USER"
provider = "google"
providerId = "111061868356786625939"
```
