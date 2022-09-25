# @MVC 구현하기

## 🚀 1단계 - @MVC 프레임워크 구현하기

- [X] AnnotationHandlerMappingTest가 정상 동작한다.
- [X] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여 AnnotationHandlerMapping과 ManualHandlerMapping 둘다 처리할 수 있다.
- [X] HTTP 메서드와 URL를 매핑 조건을 어노테이션으로 컨트롤러에 적용할 수 있다.

### 구현한 기능
- `AnnotationHandlerMapping`에서 `Map<HandlerKey, HandlerExecution>` 를 초기화한다.
- `HandlerAdapter`: `AnnotationHandlerMapping`, `ManualHandlerMapping`에 맞는 Adapter 기능을 수행하고 `ModelAndView`를 반환한다.
- `JspView`: `DispatcherServlet`이 수행하던 View 관련 기능을 수행한다.

## 🚀 2단계 - 점진적인 리팩토리

### 기능 요구 사항
- Legacy MVC와 @MVC 통합하기

컨트롤러 인터페이스 기반 MVC 프레임워크와 @MVC 프레임워크가 공존하도록 만들자.
예를 들면, 회원가입 컨트롤러를 아래처럼 어노테이션 기반 컨트롤러로 변경해도 정상 동작해야 한다.

### 체크리스트

- [x] ControllerScanner 클래스에서 @Controller가 붙은 클래스를 찾을 수 있다.
  - 컨트롤러를 찾아서 인스턴스를 생성한다.
- [x] HandlerMappingRegistry 클래스에서 HandlerMapping을 처리하도록 구현했다.
- [ ] HandlerAdapterRegistry 클래스에서 HandlerAdapter를 처리하도록 구현했다.
