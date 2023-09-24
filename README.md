# @MVC 구현하기

1단계 - @MVC 프레임워크 구현하기
- [x] 어노테이션 기반의 MVC 프레임워크를 구현한다
  - AnnotionHanlderMapping.java
    - [x] HandlerKey 만들기
    - [x] HandlerExecution 만들기
  - HandlerExecution.java
    - [x] HandlerExecution이 작동할 수 있도록 만들기

2단계 - 점진적인 리팩터링
- [x] Legacy MVC와 @MVC 통합하기
  - [x] 1단계 피드백 적용
  - [x] 어노테이션도 똑같이 작동하도록 하는 기능 추가
  - [x] RegisterController 적용해보기
  - [x] RegisterViewController의 역할을 RegisterController로 이동하고, 파일 삭제하기

3단계 - JSON View 구현하기
- [x] JspView 클래스를 구현한다
  - [x] JspView 클래스 구현 (2단계에서 완료)
  - [x] DispatcherServlet의 service 메서드에 있는 JspView가 가지고 있어야할 로직 제거하기
- [x] 리팩터링 방식을 변경한다
  - [x] DispatcherServlet에 레거시 코드를 완전히 삭제한다
- [x] JsonView 클래스 구현
  - [x] ObjectMapper를 통해 json을 반환하도록 한다.
  - [x] UserController를 추가한다
- [ ] Legacy MVC 제거
  - [x] com.techcourse에 있는 Controller를 모두 어노테이션 기반 컨트롤러로 변경한다
  - [x] "/" 경로를 받아주는 HomeController 생성하고, 컨트롤러 기반 ForwardController 주석 처리
