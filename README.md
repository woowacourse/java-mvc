# @MVC 구현하기

## 1단계 요구사항

- [x] AnnotationHandlerMappingTest가 정상 동작한다.
    - [x] AnnotationHandlerMapping이 @Controller가 붙은 클래스들을 불러온다.
    - [x] AnnotationHandlerMapping이 @Controller가 붙은 클래스 중 @RequestMapping 메서드들을 불러온다.
    - [x] 불러온 클래스 정보와 메서드 정보로 HandlerKey와 HandlerExecution을 초기화한다.
    - [x] HandlerExecution이 핸들러의 메서드를 실행시켜서 ModelAndView를 반환한다.
- [ ] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여 AnnotationHandlerMapping과 ManualHandlerMapping 둘다 처리할 수 있다.
