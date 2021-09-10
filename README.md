# MVC 프레임워크 구현하기

### 요구사항
* [x] 애노테이션 기반의 MVC 프레임워크 구현
  * [x] 비지니스 로직과의 분리
  * [x] URL과 HTTP 메소드(GET,POST,PUT,DELETE .. ) 을 매핑 조건에 포함
  * [x] 애노테이션 기반의 MVC 프레임워크 구현 후에도 Controller 기반의 MVC 프레임워크 코드가 정상 동작해야함
    * [x] 레거시 코드인 Controller 기반의 MVC 프레임워크가 동작해야함
    * [x] 레거시 코드를 건드리지 않고 DispatcherServlet에서 공존하도록 코드 추가

### 리팩토링
* [ ] DispatcherServlet에 있는 레거시 HandlerMapping을 위한 instanceof 연산자 사용 없애보기