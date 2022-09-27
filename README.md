# @MVC 구현하기

## 1단계 요구사항

- [x] AnnotationHandlerMappingTest가 정상 동작한다.
    - [x] AnnotationHandlerMapping이 @Controller가 붙은 클래스들을 불러온다.
    - [x] AnnotationHandlerMapping이 @Controller가 붙은 클래스 중 @RequestMapping 메서드들을 불러온다.
    - [x] 불러온 클래스 정보와 메서드 정보로 HandlerKey와 HandlerExecution을 초기화한다.
    - [x] HandlerExecution이 핸들러의 메서드를 실행시켜서 ModelAndView를 반환한다.
- [x] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여 AnnotationHandlerMapping과 ManualHandlerMapping 둘다 처리할 수 있다.
    - [x] handlerMapping에서 반환한 핸들러가 Controller 타입이면 Controller의 execute를 실행시키고, HandlerExecution 타입이면 HandlerExection의
      handle를 실행시킨다.
    - [x] jspView를 구현한다.
    - [x] DisPatcherServlet에서 핸들러 매핑 구현체가 동작되는 것에 상관없이 뷰를 렌더링하는 과정을 통일한다.

## 2단계 요구사항
- [x] ControllerScanner 클래스에서 @Controller가 붙은 클래스를 찾아온다.
- [x] HandlerAdpater를 이용하여 여러 타입의 핸들러를 실행시키게 한다.
- [x] HandlerMappingRegistry를 이용하여 여러 타입의 핸들러 매핑을 관리한다.
- [x] HandlerAdapterRegistry 이용하여 여러 타입의 핸들러 어댑터들을 관리한다.

## 3단계 요구사항
- [x] JspView를 구현하여 JspView가 렌더링하게 한다.
- [ ] JsonView를 구현해 Json 형식으로 요청이 들어올 때 바인딩한다.
- [ ] 모든 컨트롤러를 어노테이션 기반으로 변경한다.
