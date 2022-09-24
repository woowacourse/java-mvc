# @MVC 구현하기

### 1단계
- [X] 어노테이션 기반의 MVC 프레임워크로 개선
- [X] URL을 컨트롤러에 매핑하면서 HTTP 메서드(GET, POST, PUT, DELETE 등)도 매핑 조건에 포함시킨다.
- [X] handlerMapping, handlerAdapter 구현
  - handelrMapping에서 해당 요청을 처리할 수 있는 handler를 찾습니다.
  - 위에서 찾은 handler를 실행시킬 수 있는 handlerAdapter를 찾습니다.
    - AnnotationHandlerAdapter
      - handlerAdapter가 `@RequestMapping`이 붙은 메서드를 실행시켜 ModelAndView를 반환합니다.
    - ManualHandlerAdapter
      - handlerAdapter가 viewName으로 JspView를 생성하고 이를 활용하여 ModelAndView를 반환합니다.
  - 반환된 view를 forward()하거나 sendRedirect()합니다. 

### 2단계
- [ ] ControllerScanner 클래스에서 @Controller가 붙은 클래스를 찾을 수 있다.
- [ ] HandlerMappingRegistry 클래스에서 HandlerMapping을 처리하도록 구현
- [ ] HandlerAdapterRegistry 클래스에서 HandlerAdapter를 처리하도록 구현