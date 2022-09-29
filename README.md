# @MVC 구현하기

## 기능 요구사항 목록

### 1단계 - @MVC 프레임워크 구현하기

- [x] 어노테이션 기반의 MVC 프레임워크를 구현한다.
  - [x] DispatcherServlet에서 AnnotationHandlerMapping를 처리할 수 있다.
  - [x] DispatcherServlet에서 ManualHandlerMapping를 처리할 수 있다.

### 2단계 - 점진적인 리팩터링

- [x] ControllerScanner 클래스에서 @Controller가 붙은 클래스를 탐색한다.
- [x] HandlerMappingRegistry 클래스에서 HandlerMapping을 처리한다.
- [x] HandlerAdapterRegistry 클래스에서 HandlerAdapter를 처리한다.

### 3단계 - JSON View 구현하기

- [x] `UserController`가 JSON 형태로 응답을 반환한다.
  - [x] DispatcherServlet 클래스의 service 메서드에서 뷰에 대한 처리를 하는 부분을 `JspView` 클래스에 위임한다.
  - [x] HTTP Request Body로 JSON 타입의 데이터를 처리하는 부분을 `JsonView` 클래스에 위임한다.
- [x] 레거시 코드를 삭제하고 서버를 띄워도 정상 동작한다.
  - [x] app 모듈에 있는 모든 컨트롤러를 어노테이션 기반 MVC로 변경한다.
  - [x] asis 패키지에 있는 레거시 코드를 삭제한다.
