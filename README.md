# 만들면서 배우는 스프링

## @MVC 구현하기

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

## 기능 구현 목록

- [x] 핸들러 매핑 구현
- [x] jsp view 구현
- [x] 어노테이션 기반 컨트롤러 변경해도 정상 동작하도록 구현
- [x] handlerMapping 추상화

1. JsonView 클래스 구현

- [ ] HTML 이외에 JSON으로도 응답
- [ ] JSON으로 응답할 때 ContentType은 MediaType.APPLICATION_JSON_UTF8_VALUE으로 반환해야 한다.
- [ ] model에 데이터가 1개면 값을 그대로 반환하고 2개 이상이면 Map 형태 그대로 JSON으로 변환해서 반환한다.

2. Legacy MVC 제거

- [ ] app 모듈에 있는 모든 컨트롤러를 어노테이션 기반 MVC로 변경
- [ ] asis 패키지에 있는 레거시 코드를 삭제해도 서비스가 정상 동작하도록 리팩터링
- [ ] Legacy MVC를 제거하고 나서 DispatcherServlet도 app 패키지가 아닌 mvc 패키지로 이동

## 리팩토링 목록

- [x] 주석 제거
