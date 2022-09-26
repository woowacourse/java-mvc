# @MVC 구현하기

## step1 요구 사항 
- [x] AnnotationHandlerMappingTest가 정상 동작한다.
- [x] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여 AnnotationHandlerMapping과 ManualHandlerMapping 둘다 처리할 수 있다.
    - [x] DispatcherServlet에서 ModelAndView를 처리한다. 

## step2 요구 사항 
- [x] ControllerScanner 클래스에서 @Controller가 붙은 클래스를 찾을 수 있다. 
- [x] HandlerMappingRegistry 클래스에서 HandlerMapping을 처리하도록 구현
- [x] HandlerAdapterRegistry 클래스에서 HandlerAdapter를 처리하도록 구현 

## step3 요구 사항
- [x] JspView 클래스 구현하기 
- [x] JsonView 클래스 구현하기 
  - [x] 힌트에서 제공한 UserController 컨트롤러가 json 형태로 응답을 처리할 수 있도록 한다.
- [x] Legacy MVC 제거하기 
  - [x] 레거시 코드를 삭제하고 서버를 띄워도 정상 동작한다.







