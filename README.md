# @MVC 구현하기

---

## 1단계 구현 내용 정리

- [x] AnnotationHandlerMappingTest가 정상 동작한다.
    - [x] 어노테이션 기반으로 HTTP 메서드와 URL에 따라 컨트롤러를 매핑해줄 수 있다.
- [x] DispatcherServlet 에서 HandlerMapping 인터페이스를 활용하여 AnnotationHandlerMapping과 ManualHandlerMapping 둘다 처리할 수 있다.
    - [x] DispatcherServlet 에서 instanceof 를 이용하여 두 가지 HandlerMapping 을 처리해준다.
- [x] JspView 를 이용해서 redirect 혹은 forward 를 해줄 수 있다.

## 2단계 구현 내용 정리

- [x] ControllerScanner 에서 @Controller 가 붙은 클래스를 찾는다.
    - [x] AnnotationHandlerMapping 에 있는 Controller 찾는 로직을 ControllerScanner 로 이동시킨다.
        - [x] ControllerScanner 클래스를 생성한다.
        - [x] 리플랙션으로 @Controller 가 선언된 객체들을 찾는다.
        - [x] 각 클래스별 인스턴스를 만들어서 가지고 있는다.
- [x] HandlerMappingRegistry class 에서 HandlerMapping 을 처리하도록 구현했다.
- [x] HandlerAdapterRegistry class 에서 HandlerAdapter 를 처리하도록 구현했다.

## 3단계 구현 내용 정리 👻

- [x] JspView 구현 (이미 함)
- [ ] 힌트에서 제공한 UserController 컨트롤러가 json 형태로 응답을 반환한다.
    - restController 와 유사하게 바꿔보는 느낌인듯 ?
        - [x] user Controller 추가하기
        - [x] restController interface 추가
        - [ ] JsonView 구현하기
            - [ ] JSON으로 응답할 때 ContentType은 MediaType.APPLICATION_JSON_UTF8_VALUE으로 반환해야 한다.
            - [ ] model에 데이터가 1개면 값을 그대로 반환하고 2개 이상이면 Map 형태 그대로 JSON으로 변환해서 반환한다.
- [ ] 레거시 코드를 삭제하고 서버를 띄워도 정상 동작한다. - [ ]app 모듈에 있는 모든 컨트롤러를 어노테이션 기반 MVC로 변경한다.
    - [ ] asis 패키지에 있는 레거시 코드를 삭제해도 서비스가 정상 동작하도록 리팩터링

/
