# @MVC 구현하기

## 1단계 - @MVC 프레임워크 구현하기

### 요구사항

- [x] 어노테이션 기반으로 개선
- [x] URL을 컨트롤러에 매핑
    - HTTP Method 포함

### 체크리스트

- [x] AnnotationHandlerMappingTest가 정상 동작한다.
- [x] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여 AnnotationHandlerMapping과 ManualHandlerMapping 둘다 처리할 수 있다.

## 2단계 - 점진적인 리팩터링

### 요구사항
- [x] Legacy MVC, @MVC 통합

### 체크리스트
- [x] ControllerScanner 클래스에서 @Controller가 붙은 클래스를 찾을 수 있다.
- [x] HandlerMappingRegistry 클래스에서 HandlerMapping을 처리하도록 구현했다.
- [x] HandlerAdapterRegistry 클래스에서 HandlerAdapter를 처리하도록 구현했다.
