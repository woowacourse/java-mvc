# 1단계 - MVC 프레임워크 구현하기

## MVC 패키지

- [x] **AnnotationHandlerMapping 구현하기**
    - **handler 등록 과정**
    - 클래스의 @Contoller 와 @RequestMapping 을 찾는다.
    - 두 정보를 참고하여 default url 을 구한다.
    - 클래스 안에서 @RequestMapping 가 붙은 메서드를 찾는다.
    - 어노테이션의 value 를 참고하여 url 를 만들고 method 를 참고하여 HandlerExecution 을 등록한다.
- [x] **HandlerExecutionAdapter 구현하기**
- [ ] **DispatcherServlet 에서 HandlerExecution 과 Controller 를 찾는 분기 만들기**

## APP 패키지

- [ ] DispatcherServlet 에 AnnotationHandlerMapping 등록하기

