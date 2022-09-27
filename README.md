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

## 3단계 - JSON View 구현하기

### 요구사항

- [ ] JspView 구현
- [ ] JsonView 구현
- [ ] Legacy MVC 제거
    - [ ] app 모듈 내 Controller 클래스를 어노테이션 기반으로 수정
    - [ ] asis 패키지 내 클래스 삭제 시 동작 여부 확인

### 체크리스트 
- [ ] 힌트에서 제공한 UserController 컨트롤러가 json 형태로 응답을 반환한다.
- [ ] 레거시 코드를 삭제하고 서버를 띄워도 정상 동작한다.
