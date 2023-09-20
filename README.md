# @MVC 구현하기

- [x] DispatcherServlet에서 AnnotationHandlerMapping initialize()
- [x] 기존 ManualHandlerMapping과 AnnotationHandlerMapping을 HandlerMapping으로 추상화
- [x] HandlerMapping을 이용해 DispatcherServlet에서 Handler를 찾아 Controller나 HandlerExecutionHandler로 반환
- [x] HandlerExecutionHandler에서 Handler를 실행하고 ModelAndView를 반환