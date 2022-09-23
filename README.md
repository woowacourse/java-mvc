# @MVC 구현하기

## 기능 요구사항 목록

### 1단계 - @MVC 프레임워크 구현하기

- [x] 어노테이션 기반의 MVC 프레임워크를 구현한다.
  - [x] DispatcherServlet에서 AnnotationHandlerMapping를 처리할 수 있다.
  - [x] DispatcherServlet에서 ManualHandlerMapping를 처리할 수 있다.

### 2단계 - 점진적인 리팩터링

- [x] ControllerScanner 클래스에서 @Controller가 붙은 클래스를 탐색한다.
- [x] HandlerMappingRegistry 클래스에서 HandlerMapping을 처리한다.
- [ ] HandlerAdapterRegistry 클래스에서 HandlerAdapter를 처리한다.
