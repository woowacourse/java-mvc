## 기능 요구 사항

### 1단계
- [x] 1\. @MVC Framework 테스트 통과하기
  - [x] `AnnotationHandlerMappingTest`를 통과한다.
  - [x] HTTP 메서드(GET, POST, PUT, DELETE 등)도 매핑 조건에 포함시키자.
  - [x] `@RequestMapping`에 method 설정이 되어 있지 않으면 모든 HTTP method를 지원해야 한다.
  - [x] HTTP 메서드와 URL를 매핑 조건으로 만들어보자.
- [x] 2\. JspView 클래스를 구현한다.

### 2단계 - Legacy MVC와 @MVC 통합하기
- [x] app 모듈의 컨트롤러를 `@Controller` 애너테이션 기반으로 변경한다.
  - [x] `ControllerScanner` 클래스 추가
  - [x] `HandlerMapping` 인터페이스 추가
  - [x] `HandlerMappingRegistry` 클래스 추가
  - [x] `HandlerAdapterRegistry` 클래스 추가
