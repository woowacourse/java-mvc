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


# 요구사항 목록

## STEP 1

비즈니스 로직 구현에만 집중 할 수 있도록 어노테이션 기반의 MVC 프레임워크로 개선해보자.
그리고 URL을 컨트롤러에 매핑하면서 HTTP 메서드(GET, POST, PUT, DELETE 등)도 매핑 조건에 포함시키자.
@RequestMapping()에 method 설정이 되어 있지 않으면 모든 HTTP method를 지원해야 한다.
HTTP 메서드와 URL를 매핑 조건으로 만들어보자.
mvc 모듈은 프레임워크, app 모듈은 프로덕션 영역

- [x] MVC Framework 테스트 통과
  - `AnnotationHandlerMappingTest` 통과
  - `@RequestMapping()` method 설정이 되어 있지 않으면 모든 HTTP method를 지원
- [x] `JspView` 클래스 구현 
  - `DispatcherServlet.service`에서 어떤 부분이 뷰에 대한 처리를 하고 있는지 파악해서 JspView 클래스로 옮겨보자.

## STEP 2
- [ ] Legacy MVC와 @MVC 통합
  - 컨트롤러 인터페이스 기반 MVC 프레임워크와 @MVC 프레임워크가 공존
```java
@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest req, HttpServletResponse res) {
        ...
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req, HttpServletResponse res) {
        ...
    }
}
```
