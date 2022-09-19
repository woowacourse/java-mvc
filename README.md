# @MVC 구현하기

## 🚀 1단계 - @MVC 프레임워크 구현하기

- [X] AnnotationHandlerMappingTest가 정상 동작한다.
- [X] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여 AnnotationHandlerMapping과 ManualHandlerMapping 둘다 처리할 수 있다.
- [X] HTTP 메서드와 URL를 매핑 조건을 어노테이션으로 컨트롤러에 적용할 수 있다.

### 구현한 기능
- `AnnotationHandlerMapping`에서 `Map<HandlerKey, HandlerExecution>` 를 초기화한다.
- `HandlerAdapter`: `AnnotationHandlerMapping`, `ManualHandlerMapping`에 맞는 Adapter 기능을 수행하고 `ModelAndView`를 반환한다.
- `JspView`: `DispatcherServlet`이 수행하던 View 관련 기능을 수행한다.
