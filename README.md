# @MVC 구현하기

# 기능 요구사항 명세
- [ ] 어노테이션 기반의 MVC 프레임워크를 구현한다.
  - [ ] mvc 모듈과 app 모듈의 영역이 잘 구분되어야 한다.
  - [ ] 새로운 컨트롤러가 생겨도 MVC 프레임워크 영역까지 영향이 미치면 안된다.

<br>

- step1 @MVC 프레임워크 구현하기
  - [x] AnnotationHandlerMapping 구현 사항
    - [x] initialize 기능 구현
      - [x] basePackage 하위에 존재하는 @Controller 어노테이션 선언 클래스 탐색
      - [x] Controller 클래스 내 선언된 @requestMapping 어노테이션 선언 메서드 탐색
      - [x] 각 Method 객체를 조합으로 가지는 HandlerExecution 객체를 생성해서 저장
    - [x] getHandler 기능 구현
      - [x] HttpServletRequest 객체를 인자로 받아서 HandlerKey 객체 생성
      - [x] 생성된 HandlerKey 객체를 key로 가지는 value 값에 해당하는 handler를 반환
  - [x] HandlerExecution 구현 사항
    - [x] handler 객체에서 request, response 객체를 인자로 받아 적합한 비즈니스 로직을 수행하는 기능을 구현한다.

<br>

- step2 점진적인 리팩터링
  - [x] Legacy MVC와 @MVC 통합 구현사항
    - [x] DispatcherServlet 초기화 시 기존 handlerMapping과 더불어 AnnotationHandlerMapping도 함께 초기화
    - [x] DispatcherServlet 클래스 내 service 메서드 기능 추가 구현
      - [x] manualHandlerMapping 탐색 시 매핑되는 controller가 없는 경우 AnnotationHandlerMapping 탐색
  - [x] HandlerMapping 인터페이스 추상화
    - [x] initialize() 기능
    - [X] getHandler() 기능
  - [x] HandlerExecution 인터페이스 추상화
    - [x] handle() 기능
  - [x] HandlerMappingAdapter, HandlerExecutionAdapter 클래스로 DispatcherServlet에서 하나의 타입으로 로직을 수행할 수 있도록 리팩토링
