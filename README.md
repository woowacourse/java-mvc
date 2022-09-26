# @MVC 구현하기

### step 1
- [x] AnnotationHandlerMappingTest 가 정상 동작한다.
- [x] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여 AnnotationHandlerMapping과 ManualHandlerMapping 둘다 처리할 수 있다.

### step2
- [x] ControllerScanner 클래스에서 @Controller가 붙은 클래스를 찾을 수 있다.
- [x] HandlerMappingRegistry 클래스에서 HandlerMapping을 처리하도록 구현했다.
- [x] HandlerAdapterRegistry 클래스에서 HandlerAdapter를 처리하도록 구현했다.

### step3

- [x] JspView 클래스를 구현한다.
  - nextstep.jwp.mvc.view 패키지에서 JspView 클래스를 찾을 수 있다.  
    DispatcherServlet 클래스의 service 메서드에서 어떤 부분이 뷰에 대한 처리를 하고 있는지 파악해서 JspView 클래스로 옮겨보자.
- [x] JsonView 클래스를 구현한다.
  - nextstep.jwp.mvc.view 패키지에서 JsonView 클래스를 찾을 수 있다.  
    HTTP Request Body로 JSON 타입의 데이터를 받았을 때 어떻게 자바에서 처리할지 고민해보고 JsonView 클래스를 구현해보자.
- [ ] Legacy MVC 제거하기
  - app 모듈에 있는 모든 컨트롤러를 어노테이션 기반 MVC로 변경한다.  
  그리고 asis 패키지에 있는 레거시 코드를 삭제해도 서비스가 정상 동작하도록 리팩터링하자.
- [ ] 힌트에서 제공한 UserController 컨트롤러가 json 형태로 응답을 반환한다.
- [ ] 레거시 코드를 삭제하고 서버를 띄워도 정상 동작한다.
