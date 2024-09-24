## 기능 요구 사항

### 1단계
- [x] 1\. @MVC Framework 테스트 통과하기
  - [x] `AnnotationHandlerMappingTest`를 통과한다.
  - [x] HTTP 메서드(GET, POST, PUT, DELETE 등)도 매핑 조건에 포함시키자.
  - [x] `@RequestMapping`에 method 설정이 되어 있지 않으면 모든 HTTP method를 지원해야 한다.
  - [x] HTTP 메서드와 URL를 매핑 조건으로 만들어보자.
- [x] 2\. JspView 클래스를 구현한다.

### 2단계 - Legacy MVC와 @MVC 통합하기
- [ ] app 모듈의 컨트롤러를 `@Controller` 애너테이션 기반으로 변경한다.
  - [ ] `DispathcerServlet`의 필드에 `List<HandlerMapping>`과 `List<HandlerAdapter>`를 선언한다. 
  - [ ] `handlerMappings`에서 request path를 지원하는 핸들러를 찾는다.
  - [ ] `handlerAdapters`에서 해당 핸들러를 지원하는 `handlerAdapter`를 찾는다.
  - [ ] `handlerAdapter`에 해당 핸들러를 전달해 실행한다.
  - [ ] `handlerAdapter`가 반환한 `ModelAndView` 객체를 렌더링한다.
