# @MVC 구현하기
## 1단계 - `@MVC` 구현하기
- `AnnotationHandlerMappingTest`가 정상 동작한다.
- `DispatcherServlet`에서 `HandlerMapping` 인터페이스를 활용하여 `AnnotationHandlerMapping`과 `ManualHandlerMapping` 둘다 처리할 수 있다.

## 2단계 - 점진적인 리팩터링
- ControllerScanner 클래스에서 `@Controller`가 붙은 클래스를 찾을 수 있다.
- HandlerMappingRegistry 클래스에서 `HandlerMapping`을 처리하도록 구현했다.
- HandlerAdapterRegistry 클래스에서 `HandlerAdapter`를 처리하도록 구현했다.

## 3단계 - JSON View 구현하기
- 힌트에서 제공한 UserController 컨트롤러가 json 형태로 응답을 반환한다.
  - JSON으로 응답할 때 `ContentType`은 `MediaType.APPLICATION_JSON_UTF8_VALUE`으로 반환해야 한다.
  - model에 데이터가 1개면 값을 그대로 반환하고 2개 이상이면 Map 형태 그대로 `JSON`으로 변환해서 반환한다.
- 레거시 코드를 삭제하고 서버를 띄워도 정상 동작한다.
