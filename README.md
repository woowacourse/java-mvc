# 만들면서 배우는 스프링

## @MVC 구현하기

### 1단계 요구사항

- [x] 학습 테스트 진행하기
    - [x] [Junit3TestRunner](study/src/test/java/reflection/Junit3TestRunner.java)
    - [x] [Junit4TestRunner](study/src/test/java/reflection/Junit4TestRunner.java)
    - [x] [ReflectionTest](study/src/test/java/reflection/ReflectionTest.java)
    - [x] [ReflectionsTest](study/src/test/java/reflection/ReflectionsTest.java)

    <br>

- [x] @MVC 프레임워크 테스트 통과하기
    - [x] AnnotationHandlerMappingTest get(), post() 통과하기

    <br>

- [x] JSPView 클래스 구현하기

    <br>

- [x] @RequestMapping()에 method 설정이 되어 있지 않으면 모든 HTTP method를 지원하기
- [x] 예외 처리
    - [x] 존재하지 않는 엔드포인트로 메서드 요청이 들어오면 예외를 반환한다.
    - [x] 지원하지 않는 메서드 요청이 들어오면 예외를 반환한다.

<br>

### 2단계 요구사항

- [ ] AnnotationHandlerMapping 클래스 리팩토링
    - [ ] initialize 메서드 리팩터링
    - [ ] ControllerScanner 클래스 추가
        - [ ] HandlerExecution 생성 메서드 구현
- [x] DispatcherServlet 클래스 구현
    - [x] HandlerMapping 인터페이스 구현
    - [x] HandlerAdapter 인터페이스 구현

    <br>

### 3단계 요구사항

- [ ] JsonView 클래스 구현
    - [ ] HTML 이외에 JSON으로 응답할 수 있도록
    - [x] UserController 추가
- [x] Legacy MVC 제거
    - [x] app 모듈에 있는 모든 컨트롤러를 어노테이션 기반 MVC로 변경
    - [x] asis 패키지에 있는 레거시 코드를 삭제해도 서비스가 정상 동작하도록 리팩터링
    - [X] DispatcherServlet app 패키지 -> mvc 패키지로 이동

<br>

### 학습목표

- @MVC를 구현하면서 MVC 구조와 MVC의 각 역할을 이해한다.
- 새로운 기술을 점진적으로 적용하는 방법을 학습한다.

### 시작 가이드

1. 미션을 시작하기 전에 학습 테스트를 먼저 진행합니다.
    - [x] [Junit3TestRunner](study/src/test/java/reflection/Junit3TestRunner.java)
    - [x] [Junit4TestRunner](study/src/test/java/reflection/Junit4TestRunner.java)
    - [x] [ReflectionTest](study/src/test/java/reflection/ReflectionTest.java)
    - [x] [ReflectionsTest](study/src/test/java/reflection/ReflectionsTest.java)
    - 나머지 학습 테스트는 강의 시간에 풀어봅시다.
2. 학습 테스트를 완료하면 LMS의 1단계 미션부터 진행합니다.

## 학습 테스트

1. [Reflection API](study/src/test/java/reflection)
2. [Servlet](study/src/test/java/servlet)
