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

## 🚀 1단계 - @MVC 프레임워크 구현하기

### 기능 요구 사항

- [x] @MVC Framework 테스트 통과하기
    - 효과적인 실습을 위해 새로운 MVC 프레임워크의 뼈대가 되는 코드(mvc 모듈의 webmvc.servlet.mvc.tobe 패키지)와 테스트 코드를 제공하고 있다.
      AnnotationHandlerMappingTest 클래스의 테스트가 성공하면 1단계 미션을 완료한 것으로 생각하면 된다.
    - Tomcat 구현하기 미션에서 적용한 Controller 인터페이스는 2단계 미션에서 통합할 예정이다. Controller 인터페이스는 그대로 두고 미션을 진행한다.
- [x] JspView 클래스를 구현한다.
    - webmvc.org.springframework.web.servlet.view 패키지에서 JspView 클래스를 찾을 수 있다.
    - DispatcherServlet 클래스의 service 메서드에서 어떤 부분이 뷰에 대한 처리를 하고 있는지 파악해서 JspView 클래스로 옮겨보자.

## 🚀 2단계 - 점진적인 리팩터링

### 기능 요구 사항

- [ ] Legacy MVC와 @MVC 통합하기 (기존 코드를 유지하면서 신규 기능을 추가)
  컨트롤러 인터페이스 기반 MVC 프레임워크와 @MVC 프레임워크가 공존하도록 만들자.
  예를 들면, 회원가입 컨트롤러를 아래처럼 어노테이션 기반 컨트롤러로 변경해도 정상 동작해야 한다.

```java

@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest req, HttpServletResponse res) {...}

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req, HttpServletResponse res) {...}
}
```
