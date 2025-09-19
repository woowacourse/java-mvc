# 🪐@MVC 구현하기

### 1단계 - @MVC 프레임워크 구현하기

- [x] @RequestMapping의 URL + HTTP 메서드에 대한 실팽 Controller 클래스를 매핑할 수 있다.
- [x] @RequestMapping에 메서드 설정이 되어 있지 않으면 모든 HTTP 메서드를 지원해야 한다.
- [x] DispatcherServlet.class의 뷰 처리 부분을 JspView.class로 리팩터링한다.

### 2단계 - 점진적인 리팩터링

- [x] ManualHandlerMapping / AnnotationHandlerMapping 추상화
- [x] ManualHandlerMapping / AnnotationHandlerMapping의 Controller 추상화
- [x] HandlerMappingRegistry / HandlerAdapterRegistry 추가
- [x] AnnotationHandlerMapping -> ControllerScanner 적용
