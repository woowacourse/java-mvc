# @MVC 구현하기

## [🚀 1단계 - @MVC 프레임워크 구현하기](https://techcourse.woowahan.com/s/cCM7rQR9/ls/ul3SweFH)

### 기능 요구사항
어노테이션 기반의 MVC 프레임워크를 구현한다.
- [X] `AnnotationHandlerMappingTest`가 정상 동작한다.
- [X] `DispatcherServlet`에서 `HandlerMapping` 인터페이스를 활용하여 `AnnotationHandlerMapping`과 `ManualHandlerMapping` 둘다 처리할 수 있다.

#### AnnotationHandlerMapping 구현
- [X] 특정 package 내에서 `@Controller` annotation이 달린 class를 찾는다.
- [X] controller class 내에서 `@RequestMapping` annotation이 달린 method를 찾는다.
- [X] `@RequestMapping`에서 지정한 url과 http method에 대해 `HandlerExecution`을 mapping한다.

#### DispatcherServlet 구현
`ManualHandlerMapping`과 `AnnotationHandlerMapping` 둘 다 사용할 수 있어야 한다.
- [X] `Controller`와 `HandlerExecution` 둘 다를 실행할 수 있다.
- [X] `ModelAndView`를 적절하게 rendering 할 수 있다.

## [🚀 2단계 - 점진적인 리팩터링](https://techcourse.woowahan.com/s/cCM7rQR9/ls/rn3vGCrZ)

### 기능 요구사항
interface 기반의 컨트롤러와 annotation 기반의 컨트롤러가 공존하는 상태로 정상 동작하도록 구현한다.
- [X] ControllerScanner 클래스에서 @Controller가 붙은 클래스를 찾을 수 있다.
- [X] HandlerMappingRegistry 클래스에서 HandlerMapping을 처리하도록 구현했다.
- [X] HandlerAdapterRegistry 클래스에서 HandlerAdapter를 처리하도록 구현했다.

## [🚀 3단계 - JSON View 구현하기](https://techcourse.woowahan.com/s/cCM7rQR9/ls/rBFfOujC)

### 기능 요구사항
화면에 대한 책임을 View가 가지게 하고, `JsonView`를 구현하여 REST API를 지원할 수 있도록 한다.
- [ ] 힌트에서 제공한 UserController 컨트롤러가 json 형태로 응답을 반환한다.
- [ ] 레거시 코드를 삭제하고 서버를 띄워도 정상 동작한다.

#### JspView 구현
- [X] Jsp 반환을 JspView에서 처리한다.

#### JsonView 구현
- [X] model의 객체를 json으로 변환하여 response body로 응답한다.

### Legacy MVC 제거
- [ ] app module의 모든 controller를 annotation 기반으로 변경한다.
- [ ] asis 패키지의 레거시 코드를 삭제해도 정상 동작하도록 리팩터링한다.
