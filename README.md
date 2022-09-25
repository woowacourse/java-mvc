# @MVC 구현하기
## 1단계 - `@MVC` 구현하기
- `AnnotationHandlerMappingTest`가 정상 동작한다.
- `DispatcherServlet`에서 `HandlerMapping` 인터페이스를 활용하여 `AnnotationHandlerMapping`과 `ManualHandlerMapping` 둘다 처리할 수 있다.

## 2단계 - 점진적인 리팩터링
- ControllerScanner 클래스에서 `@Controller`가 붙은 클래스를 찾을 수 있다.
- HandlerMappingRegistry 클래스에서 `HandlerMapping`을 처리하도록 구현했다.
- HandlerAdapterRegistry 클래스에서 `HandlerAdapter`를 처리하도록 구현했다.