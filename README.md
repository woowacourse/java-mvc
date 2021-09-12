# MVC 프레임워크 구현하기

## 요구사항

- [x] AnnotationHandlerMapping의 initialize()를 구현한다.
  - [x] @Controller가 있는 클래스를 찾아 @RequestMapping에 설정된 path, method를 이용해 handlerExecutions를 초기화한다.

- [x] AnnotationHandlerMapping의 getHandler()를 구현한다.
  - [x] 요청에 맞는 HandlerExecution을 반환한다.
  
- [x] DispatcherServlet에서 각 handlerMapping의 getHandler() 반환값을 처리해줄 HandlerAdapter를 구현한다.
  - [x] HandlerAdapter 내에서 getHandler() 반환값을 이용해서 handle 후 ModelAndView를 반환한다
  
- [x] JspView의 render를 구현한다.
  