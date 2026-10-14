# 만들면서 배우는 스프링

## @MVC 구현하기

### 학습목표
- @MVC를 구현하면서 MVC 구조와 MVC의 각 역할을 이해한다.
- 새로운 기술을 점진적으로 적용하는 방법을 학습한다.

### 시작 가이드
1. 미션을 시작하기 전에 학습 테스트를 먼저 진행합니다.
    - [Junit3TestRunner](study/src/test/java/reflection/Junit3TestRunner.java)
    - [Junit4TestRunner](study/src/test/java/reflection/Junit4TestRunner.java)
    - [ReflectionTest](study/src/test/java/reflection/ReflectionTest.java)
    - [ReflectionsTest](study/src/test/java/reflection/ReflectionsTest.java)
    - 나머지 학습 테스트는 강의 시간에 풀어봅시다.
2. 학습 테스트를 완료하면 LMS의 1단계 미션부터 진행합니다.

## 학습 테스트
1. [Reflection API](study/src/test/java/reflection)
2. [Servlet](study/src/test/java/servlet)

## 3단계: JSON View와 어노테이션 MVC

요청은 `DispatcherServlet → HandlerMapping → HandlerAdapter → ModelAndView → View` 순서로 처리한다.
DispatcherServlet은 mvc 모듈의 공통 기능이고, app의 DispatcherServletInitializer가
스캔할 패키지 `com.techcourse.controller`를 전달한다. 레거시 컨트롤러와 수동 매핑은 제거했다.

### JSON 응답 계약

`GET /api/user?account=gugu`는 User 도메인을 직접 반환하지 않고
`UserResponse(String account)` DTO를 모델의 user 키에 담는다.
JsonView는 모델 Map 전체를 직렬화하므로 부가 모델을 추가해도 user.account 경로가 유지된다.

```json
{"user":{"account":"gugu"}}
```

| 요청 | 상태 | 응답 |
| --- | --- | --- |
| 존재하는 account | 200 | user DTO |
| account 누락·빈 문자열·공백 | 400 | INVALID_ACCOUNT |
| 존재하지 않는 account | 404 | USER_NOT_FOUND |

오류도 ErrorResponse DTO를 사용한 JSON으로 응답한다.

```json
{"error":{"code":"INVALID_ACCOUNT","message":"account is required"}}
{"error":{"code":"USER_NOT_FOUND","message":"user not found"}}
```

account는 임의로 trim하거나 대소문자를 바꾸지 않고 조회한다.
예상 가능한 조회 오류는 앱 컨트롤러에서 HTTP 상태와 오류 모델로 변환한다.
예상하지 못한 실행·렌더링 오류는 DispatcherServlet이 ServletException으로 전달한다.

### ObjectMapper와 인코딩

JsonView의 private static final ObjectMapper를 View 인스턴스와 요청 사이에서 공유한다.
mapper는 사용 전에 설정하고 요청 처리 중 설정을 바꾸지 않는다.
Content-Type과 UTF-8은 response writer를 얻기 전에 설정한다.
JSON 직렬화와 Jackson 의존성은 mvc 모듈에 두고, 앱은 DTO와 모델을 결정한다.

### 화면 요청

- GET /는 /index.jsp로 forward하여 브라우저 주소 /를 유지한다.
- GET /login, GET /register는 화면을 forward한다.
- 로그인·가입 POST 완료 후에는 redirect하여 새로고침 시 POST 재전송을 피한다.
- 로그아웃은 세션 사용자를 제거하고 /로 redirect한다.
- 기존 /login/view, /register/view는 어노테이션 호환 메서드로 유지한다.

### 핸들러 등록

HandlerExecution 생성자가 메서드 공개 여부, 요청·응답 인자, ModelAndView 반환 타입을 검증한다.
public 계약 검증 후 반사 호출 접근성을 확보하여 비공개 인터페이스의 public default 메서드도 실행한다.
상속 매핑은 실제 override 메서드를 한 번만 등록하며, 자식·가까운 부모·인터페이스 순서로 결정한다.
서로 다른 실제 메서드의 URL/HTTP 메서드 중복과 충돌 인터페이스 매핑은 초기화 오류다.
매핑은 검증을 마친 뒤 한 번에 게시하여 실패 도중의 부분 상태를 노출하지 않는다.

### 검증

```sh
./gradlew :mvc:check :app:check :mvc:build :app:build
./gradlew :app:test --tests '*UserApiHttpTest'
./gradlew test
```

UserApiHttpTest는 임시 디렉터리와 동적 포트의 Tomcat에서 실제 HTTP 상태·JSON·UTF-8을 검증한다.
루트와 /mvc 컨텍스트에서 동일한 API 계약을 확인하고 서버를 종료한다.
전체 test에는 별도 study 학습 과제도 포함된다.

현재 전체 테스트의 study DI 학습 과제에서는 Stage3Test.stage3(), Stage4Test.stage4()가
미구현 DIContainer의 null 반환으로 실패한다. MVC·앱 빌드와 테스트는 통과한다.
