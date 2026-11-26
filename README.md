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

# @MVC 구현하기

## 1단계 - @MVC 프레임워크 구현하기
- [x] @MVC Framework 테스트 통과하기
  - [x] AnnotationHandlerMappingTest 클래스의 테스트 통과
    - [x] 어노테이션 기반의 MVC 프레임워크로 개선
    - [x] URL을 컨트롤러에 매핑하면서 HTTP 메서드(GET, POST, PUT, DELETE 등)도 매핑 조건에 포함
    - [x] @RequestMapping()에 method 설정이 되어 있지 않으면 모든 HTTP method를 지원
  - [x] Controller 인터페이스는 그대로 두고 미션 진행
- [x] JspView 클래스를 구현한다
  - [x] DispatcherServlet 클래스의 service 메서드에서 어떤 부분이 뷰에 대한 처리를 하고 있는지 파악
  - [x] 뷰에 대한 처리를 하는 부분을 JspView 클래스로 옮긴다

## 2단계 - 점진적인 리팩터링
- Legacy MVC와 @MVC 통합하기
- [x] 컨트롤러 인터페이스 기반 MVC 프레임워크와 @MVC 프레임워크가 공존한 상태로 동작 가능하다.
- [x] 컨트롤러의 인스턴스를 생성하는 ControllerScanner 클래스를 추가했다.
- [x] 컨트롤러의 메서드 정보로 HandlerExecution을 생성한다.
- [x] HandlerMapping 인터페이스로 RequestMapping, AnnotationHandlerMapping 클래스의 공통 부분을 추상화했다.
- [x] HandlerAdapter 인터페이스: 요청 URL에 따라 AnnotationHandlerMapping 또는 ManualHandlerMapping 클래스에서 찾은 컨트롤러를 실행한다.

## 3단계 - JSON View 구현하기
- [x] JsonView 클래스 구현하기: HTML 이외에 JSON으로 응답할 수 있도록 구현
    - [x] JSON을 자바 객체로 변환할 때 Jackson 라이브러리를 사용
    - [x] JSON으로 응답할 때 ContentType은 MediaType.APPLICATION_JSON_UTF8_VALUE으로 반환
    - [x] model에 데이터가 1개면 값을 그대로 반환하고 2개 이상이면 Map 형태 그대로 JSON으로 변환해서 반환
- [x] Legacy MVC 제거하기
  - [x] app 모듈에 있는 모든 컨트롤러를 어노테이션 기반 MVC로 변경
  - [x] asis 패키지에 있는 레거시 코드 삭제
  - [x] DispatcherServlet의 위치를 mvc 패키지로 이동
