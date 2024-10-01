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

### 구현할 기능 목록
### 1단계 - @MVC 프레임워크 구현하기

- [x] [AnnotationHandlerMapping](mvc/src/test/java/com/interface21/webmvc/servlet/mvc/tobe/AnnotationHandlerMappingTest.java) 테스트 통과하기
  - [x] URL을 컨틀롤러에 매핑하면서 HTTP 메서드(GET, POST, PUT, DELETE 등) 을 매핑 조건에 포함시킨다. 
  - [x] @RequestMapping()에 method 설정이 되어 있지 않으면 모든 HTTP method를 지원해야 한다. 
  - [x] @Controller 가 붙은 클래스 AnnotationHandlerMapping에 등록한다. 
- [x] [JspView 클래스](mvc/src/main/java/com/interface21/webmvc/servlet/view/JspView.java) 구현하기
  - [x] DispatchServlet 클래스의 service 메서드에서 뷰를 처리하는 부분을 JspView 클래스로 옮긴다.

### 2단계 - 점진적인 리팩터링
- [ ] Legacy MVC와 @MVC 통합하기
  - [ ] 컨트롤러 인터페이스 기반 MVC 프레임워크와 @MVC 프레임워크가 공존하게 만들자
  - [x] HandlerMapping 관리 클래스 생성
  - [ ] handler 실행을 도와주는 HandlerAdaptor 생성
