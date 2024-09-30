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

---

## 기능 구현 목록

### 1단계

- AnnotationHandlerMappingTest 통과 시키기
  - [x] AnnotationHandlerMapping initialize : 핸들러 매핑의 초기화 작업
  - [x] AnnotationHandlerMapping getHandler : 요청에 적합한 핸들러 조회
  - [x] HandlerExecution handle : 요청 처리
- [x] JspView 클래스 구현

### 2단계

- [x] login 로직 @MVC 사용으로 변경

- [x] handler 인터페이스 구현
  - 구현체: AnnotationHandlerMapping, ManualHandlerMapping

- DispatcherServlet 수정
  - [x] handler 리스트 갖도록 수정
  - [x] service 로직에서 handler 모두 실행해서 매핑 정보 가져옴

- Legacy MVC를 따르는 컨트롤러 정상 동작
  - [x] GET / -> /index.jsp
  - [x] GET /register/view -> /register.jsp
  - [x] POST /register 회원가입 성공 -> /index.jsp
  - [x] GET /logout -> 세션 정보 삭제
  - [x] GET /login/view -> /login.jsp

- @MVC 따르는 컨트롤러 정상 동작
  - [x] GET /login -> /login.jsp
  - [x] POST /login 로그인 성공 -> /index.jsp
  - [x] POST /login 로그인 실패 -> /401.jsp

- 처리 추가
  - [x] /login, /register 시 request에 param이 잘못된 경우 -> /400.jsp
  - [x] 컨트롤러 단 예외 발생 -> /500.jsp
  - [x] 페이지가 존재하지 않은 예외 발생 -> /404.jsp

- 테스트
  - [x] servlet 테스트
    - [x] 컨트롤러 정상 동작 확인

### 3단계

- [x] JsonView 클래스를 구현
  - HTML 이외에 JSON으로 응답할 수 있도록 JsonView 클래스를 구현
  - model에 데이터가 1개면 값을 그대로 반환
  - 2개 이상이면 Map 형태 그대로 JSON으로 변환해서 반환

- [x] Legacy MVC 제거
  - [x] app 모듈에 있는 모든 컨트롤러를 어노테이션 기반 MVC로 변경
  - [x] asis 패키지에 있는 레거시 코드를 삭제해도 서비스가 정상 동작하도록 리팩터링
  - [x] DispatcherServlet도 app 패키지가 아닌 mvc 패키지 이동
