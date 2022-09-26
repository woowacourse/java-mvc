# @MVC 구현하기

### 1단게 기능 구현 목록
- [x] AnnotationHandlerMapping 구현
- [x] JspView 구현
- [x] AnnotationHandlerMapping과 ManualHandlerMapping 둘 다 처리 가능

---
### 2단계 기능 구현 목록
- [x] ControllerScanner 클래스에서 @Controller가 붙은 클래스를 찾아 생성할 수 있다.
- [x] HandlerMappingRegistry 클래스에서 HandlerMapping 을 처리
- [x] HandlerAdapterRegistry 클래스에서 HandlerAdapter 를 처리
- [x] 요청받은 페이지가 없으면 404를 응답하도록 구현

### 3단계 기능 구현 목록
- [x] JsonView 클래스 구현
- [x] UserController 가 json 형태로 응답을 반환한다.
- [x] 기존 레거시 MVC를 제거하고 어노테이션 기반 MVC 로 변경한다.
