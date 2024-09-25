## 기능 요구사항 명세

- [X] @MVC 프레임워크 구현하기
  - [X] @MVC Framework 테스트 통과하기
    - [X] `AnnotationHandlerMappingTest` 클래스 테스트 통과하기
  - [X] JspView 클래스 구현하기
    - [X] `DispatcherServlet` 클래스의 `service` 메서드에서 뷰 처리 로직을 `JspView` 클래스로 옮기기
  - [X] RequestMapping에 method 설정이 되어 있지 않으면, 모든 HTTP method 지원하기
- [ ] 점진적인 리팩터링
  - [ ] `DispatcherServlet` 리팩토링
    - [X] `HandlerMapping`
      - [X] 핸들러를 반환하는 `HandlerMapping` 인터페이스 구현
      - [X] `ManualHandlerMapping` 구현체 생성
      - [X] `AnnotationHandlerMapping` 구현체 생성
    - [X] `HandlerAdapter`
      - [X] 핸들러를 실행 가능한 형태로 바꿔주는 `HandlerAdapter` 구현
      - [X] `ManualHandlerAdapter` 구현체 생성
      - [X] `AnnotationHandlerAdapter` 구현체 생성
- [ ] JSON View 구현하기