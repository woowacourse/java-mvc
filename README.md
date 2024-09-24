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

2단계 미션

`AnnotatedHandlerMapping`을 활용하여 어노테이션이 붙은 컨트롤러를 등록해야 한다.

## 흐름 분석

1. Tomcat.start() 시작
2. tomcat이 HandlerTypes 어노테이션이 붙은 클래스를 찾음
3. 찾은 클래스(mvc 내 com.interface21.web.SpringServletContainerInitializer)를 찾은 후 onStartUp() 실행
4. HandlerTypes 어노테이션 안에 있는 인터페이스 구현체 (DispatchServletInitializer)를 찾아서 onStartUp(servletContext) 실행
5. DispatchServletInitializer에서 초기화 한 DispatchServlet을 ServletContext에 등록함
6. 등록 이후 registration에서 addUrlMapping("/")를 하는데 이는 모든 하위 URL을 이 서블릿에서 처리한다는 뜻이다.[^1]
7. 모든 요청과 처리가 DispatcherServlet에서 이루어지기 때문에 `service`를 메서드를 이용해서 점진적 리팩터링하면 될 것 같다.
   [^1]: https://javaee.github.io/servlet-spec/downloads/servlet-4.0/servlet-4_0_FINAL.pdf
