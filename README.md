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

### 1단계 - @MVC 프레임워크 구현하기
- [x] @MVC Framework 테스트 통과
- [x] JspView 클래스를 구현

### 2단계 - 점진적인 리팩터링
- [x] 컨트롤러 인터페이스 기반 MVC 프레임워크와 @MVC 프레임워크가 공존하도록 구현
  - [x] 어노테이션 기반 컨트롤러 구현
  - [x] 어노테이션 기반 컨트롤러와 인터페이스 기반 컨트롤러가 같이 돌아갈 수 있다.
  - [x] testSharedCounter()의 결과 이유 적기

### 3단계 - JSON View 구현하기
- [ ] JsonView 클래스를 구현한다.
  - [ ] HTML 이외에 JSON으로 응답할 수 있도록 JsonView 클래스를 구현
  - [ ] JSON을 자바 객체로 변환할 때 Jackson 라이브러리를 사용
  - [ ] JSON으로 응답할 때 `ContentType`은 `MediaType.APPLICATION_JSON_UTF8_VALUE`으로 반환
  - [ ] model에 데이터가 1개면 값을 그대로 반환하고 2개 이상이면 Map 형태 그대로 JSON으로 변환해서 반환
- [ ] Legacy MVC 제거
  - [ ] app 모듈에 있는 모든 컨트롤러를 어노테이션 기반 MVC로 변경
  - [ ] asis 패키지에 있는 레거시 코드를 삭제해도 서비스가 정상 동작하도록 리팩터링
  - [ ] DispatcherServlet도 app 패키지가 아닌 mvc 패키지로 이동
