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

### 기능 요구 사항
- [x] 어노테이션 기반 MVC 프레임워크로 개선
  - [x] URL 매핑(value)
  - [x] HTTP 메서드 매핑(method)
    - [x] 없으면 모든 HTTP method 지원
- [x] @MVC Framework 테스트 통과하기
- [x] JspView 클래스 구현 : DispatcherServlet, service 메서드에서 어떤 부분이 뷰에 대한 처리를 하고 있는지 파악해서 JspView 클래스로 옮기기
- [x] Legacy MVC와 @MVC 통합하기
- [ ] JsonView 클래스를 구현
- [ ] Legacy MVC 제거
  - [ ] app 모듈에 있는 모든 컨트롤러를 어노테이션 기반 MVC로 변경
  - [ ] asis 패키지에 있는 레거시 코드를 삭제해도 서비스가 정상 동작하도록 리팩터링
  - [ ] Legacy MVC를 제거하고 나서 DispatcherServlet도 app 패키지가 아닌 mvc 패키지로 이동
