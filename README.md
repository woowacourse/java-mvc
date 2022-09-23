# @MVC 구현하기

## 🚀 1단계 - @MVC 프레임워크 구현하기

### 기능 요구 사항

- @MVC Freamwork: 어노테이션 기반의 MVC 프레임워크를 구현한다.

### 체크리스트

- [x] AnnotationHandlerMappingTest가 정상 동작한다.
- [x] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여 AnnotationHandlerMapping과 ManualHandlerMapping 둘다 처리할 수 있다.

## 🚀 2단계 - 점진적인 리팩터링

### 기능 요구 사항

- Legacy MVC와 @MVC 통합하기

### 체크리스트

- [ ] ControllerScanner 클래스에서 @Controller가 붙은 클래스를 찾을 수 있다.
- [ ] HandlerMappingRegistry 클래스에서 HandlerMapping을 처리하도록 구현했다.
- [ ] HandlerAdapterRegistry 클래스에서 HandlerAdapter를 처리하도록 구현했다.

## 🚀 3단계 - JSON View 구현하기
