# @MVC 구현하기

## 1단계 체크리스트
- [X] AnnotationHandlerMappingTest가 정상 동작한다.
- [X] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여 AnnotationHandlerMapping과 ManualHandlerMapping 둘다 처리할 수 있다.

## 2단계 체크리스트
- [X] ControllerScanner 클래스에서 @Controller가 붙은 클래스를 찾을 수 있다.
- [X] HandlerMappingRegistry 클래스에서 HandlerMapping을 처리하도록 구현했다.
- [X] HandlerAdapterRegistry 클래스에서 HandlerAdapter를 처리하도록 구현했다.

## 3단계 체크리스트
- [X] 힌트에서 제공한 UserController 컨트롤러가 json 형태로 응답을 반환한다.
- [X] 레거시 코드를 삭제하고 서버를 띄워도 정상 동작한다.
