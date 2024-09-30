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

<br>

---

## 1단계

### 학습 테스트

- [x] [Junit3TestRunner](study%2Fsrc%2Ftest%2Fjava%2Freflection%2FJunit3TestRunner.java)
- [x] [Junit4TestRunner](study%2Fsrc%2Ftest%2Fjava%2Freflection%2FJunit4TestRunner.java)
- [x] [ReflectionTest](study%2Fsrc%2Ftest%2Fjava%2Freflection%2FReflectionTest.java)
- [x] [ReflectionsTest](study%2Fsrc%2Ftest%2Fjava%2Freflection%2FReflectionsTest.java)

### 미션

- [x] URL을 컨트롤러에 매핑하면서 HTTP 메서드(GET, POST, PUT, DELETE 등)도 매핑 조건에 포함
- [x] @RequestMapping()에 method 설정이 되어 있지 않으면 모든 HTTP method를 지원
- [x] [AnnotationHandlerMappingTest](mvc%2Fsrc%2Ftest%2Fjava%2Fcom%2Finterface21%2Fwebmvc%2Fservlet%2Fmvc%2Ftobe%2FAnnotationHandlerMappingTest.java) 테스트 통과
- [x] JspView 클래스 구현

<br>

## 2단계

### 학습 테스트

- [x] [FilterTest](study%2Fsrc%2Ftest%2Fjava%2Fservlet%2Fcom%2Fexample%2FFilterTest.java)
- [x] [ServletTest](study%2Fsrc%2Ftest%2Fjava%2Fservlet%2Fcom%2Fexample%2FServletTest.java)

### 미션 

- [x] Legacy MVC와 @MVC 통합

<br>

## 3단계

### 학습 테스트

### 미션

- [x] JsonView 클래스 구현
- [ ] Legacy MVC 제거
  - [x] app 모듈에 있는 모든 컨트롤러를 어노테이션 기반 MVC로 변경
  - [ ] asis 패키지에 있는 레거시 코드 삭제
  - [ ] DispatcherServlet을 mvc 패키지로 이동
