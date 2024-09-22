# 만들면서 배우는 스프링

## @MVC 구현하기 기능 요구사항

### 학습 테스트

1. Reflection API

- [x] Junit3TestRunner, Junit4TestRunner 클래스의 테스트 성공
- [x] ReflectionTest, ReflectionsTest 클래스의 테스트 성공
    - 리플렉션으로 어떤 작업을 할 수 있는지 파악
    - 라이브러리, 프레임워크에서 어떻게 활용할 수 있는지 파악

2. Servlet과 Servlet Container

- [x] ServletTest 클래스의 테스트 성공
    - SharedCounterServlet, LocalCounterServlet 클래스의 차이점
    - init, service, destroy 메서드가 각각 언제 실행되는지 콘솔 로그에서 확인
- [x] FilterTest 클래스의 테스트 성공
    - doFilter 메서드는 어느 시점에 실행될까?
    - 왜 인코딩을 따로 설정해줘야 할까?

### 1단계 - @MVC 프레임워크 구현하기

1. @MVC Framework 테스트 통과하기

- [ ] URL을 컨트롤러에 매핑하면서 HTTP 메서드(GET, POST, PUT, DELETE 등)도 매핑 조건에 포함
- [ ] @RequestMapping()에 method 설정이 되어 있지 않으면 모든 HTTP method 지원
- [ ] HTTP 메서드와 URL를 매핑 조건으로 하는 어노테이션 기반의 MVC 프레임워크 구현
    - 기존의 Controller 인터페이스를 활용한 MVC 프레임워크를 어노테이션 기반의 MVC 프레임워크로 개선
    - Tomcat 구현하기 미션에서 적용한 Controller 인터페이스는 2단계 미션에서 통합할 예정
- [ ] AnnotationHandlerMappingTest 클래스의 테스트 성공

2. JspView 클래스를 구현한다

- [ ] DispatcherServlet 클래스의 service 메서드에서 뷰에 대한 처리 하는 부분을 JspView 클래스로 이동

### 2단계 - 점진적인 리팩터링

### 3단계 - JSON View 구현하기

## @MVC 구현하기 가이드

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
