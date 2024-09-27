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

## 기능 구현 목록

### 1단계 - @MVC 프레임워크 구현하기
- [x] @MVC Framework 테스트 통과하기
  - [x] AnnotationHandlerMapping.initialize() 및 getHandler() 구현
  - [x] HandlerExecution.handle() 구현
- [x] JspView 클래스 구현

### 2단계 - 점진적인 리팩터링

- [x] ControllerScanner 객체를 통해 컨트롤러 인스턴스 생성하는 로직 분리
  - [x] AnnotationHandlerMapping 리팩터링
- [x] Legacy MVC 와 @MVC 통합하기
  - [x] HandlerMapping 인터페이스로 AnnotationHandlerMapping 과 ManualHandlerMapping 통합
  - [x] HandlerAdaptor 인터페이스로 Handler 을 통한 메서드 실행
  - [x] 각 객체를 관리하는 registry 구현
  - [x] DispatcherServlet 리팩터링
