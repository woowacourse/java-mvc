# 1단계 - MVC 프레임워크 구현하기

## MVC 패키지

- [x] **AnnotationHandlerMapping 구현하기**
    - **handler 등록 과정**
    - 클래스의 @Contoller 와 @RequestMapping 을 찾는다.
    - 두 정보를 참고하여 default url 을 구한다.
    - 클래스 안에서 @RequestMapping 가 붙은 메서드를 찾는다.
    - 어노테이션의 value 를 참고하여 url 를 만들고 method 를 참고하여 HandlerExecution 을 등록한다.
- [x] **HandlerExecutionAdapter 구현하기** (AnnotationHandlerMapping 이용)
- [x] **ManualHandlerAdapter 구현하기** (ManualHandlerMapping 이용)
- [x] **ModelAndView 와 JspView 채우기**
- [x] **DispatcherServlet 에서 HandlerExecution 과 Controller 를 선택하는 동작 구현**


## APP 패키지
- [x] AppWebApplicationInitializer 에서 ManualHandlerAdapter 적용하여 레거시 코드 돌아가게 하기
- [x] AnnotationHandlerMapping 과 HandlerExecutionAdapter 등록 및 어노테이션 기반 컨트롤러 구현

# 2단계 - 리팩터링
- [x] handlerMappings 일급컬렉션으로 분리
- [x] handlerAdapters 일급컬렉션으로 분리
- [x] AnnotationHandlerMapping 리팩터링
  - ControllerScanner 객체 분리
  - extract 대신 register 메서드로 변경
 
# 3단계 - View 구현하기**
- [x] ObjectMapper 를 이용하여 객체를 Json 으로 변환하는 JsonConverter 구현

