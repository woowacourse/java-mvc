# @MVC 구현하기

## 🚀 1단계 - @MVC 프레임워크 구현하기

- [X] AnnotationHandlerMappingTest가 정상 동작한다.
- [ ] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여 AnnotationHandlerMapping과 ManualHandlerMapping 둘다 처리할 수 있다.

### 구현한 기능
- `AnnotationHandlerMapping`에서 `Map<HandlerKey, HandlerExecution>` 를 초기화한다.
