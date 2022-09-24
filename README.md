# @MVC 구현하기

---

## 1단계 구현 내용 정리

- [x] AnnotationHandlerMappingTest가 정상 동작한다.
    - [x] 어노테이션 기반으로 HTTP 메서드와 URL에 따라 컨트롤러를 매핑해줄 수 있다.
- [x] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여 AnnotationHandlerMapping과 ManualHandlerMapping 둘다 처리할 수 있다.
    - [x] DispatcherServlet에서 instanceof 를 이용하여 두 가지 HandlerMapping 을 처리해준다.
- [x] JspView 를 이용해서 redirect 혹은 forward 를 해줄 수 있다.

## 2단계 구현 내용 정리

- [x] ControllerScanner 에서 @Controller 가 붙은 클래스를 찾는다.
    - [x] AnnotationHandlerMapping 에 있는 Controller 찾는 로직을 ControllerScanner 로 이동시킨다.
        - [x] ControllerScanner 클래스를 생성한다.
        - [x] 리플랙션으로 @Controller 가 선언된 객체들을 찾는다.
        - [x] 각 클래스별 인스턴스를 만들어서 가지고 있는다.
- [ ] HandlerMappingRegistry class 에서 HandlerMapping 을 처리하도록 구현했다.
- [ ] HandlerAdapterRegistry class 에서 HandlerAdapter 를 처리하도록 구현했다. 
