# @MVC 구현하기
> 나만의 @MVC 프레임워크를 만들어보자.

## 💻 기능 요구 사항

### 1단계 - @MVC 프레임워크 구현하기
> 기존의 MVC 프레임워크를 어노테이션 기반으로 변경한다.  
> - 개발자가 비지니스 로직 구현에만 집중할 수 있게 된다!

#### AnnotationHandlerMapping
- [X] `initialize()`
  - [X] `@Controller`가 있는 클래스를 컨트롤러로 인식하고 모두 찾아온다.
  - [X] 각 메서드의 `@RequestMapping` 속성을 확인해 HandlerKey와 HanderExecution을 생성 및 등록한다.
    - [X] `value`(URL), `method`(HTTP 메서드) 속성을 가진다. 
- [X] `getHandler(request)`
  - [X] request를 확인해 HandlerKey를 만든다.
  - [X] HandlerKey로 HanderExecution을 찾고 리턴한다.

#### DispatcherServlet에서 ManualHandlerMapping, AnnotationHandlerMapping 모두 지원
> AnnotationHandlerMapping도 동작함을 보이기 위해 LoginController를 어노테이션 기반으로 변경했다.
- [X] AppWebApplicationInitializer에서 DispatcherServlet에 AnnotationHandlerMapping을 추가해준다.
- [X] DispatcherServlet의 service()에서 HandlerAdapter 인터페이스를 이용하도록 한다. 
  - [X] ManualHandlerAdapter를 구현한다.
  - [X] AnnotationHandlerMapping를 구현한다.
  - [X] 뷰에 대한 처리를 JspView로 이동시킨다. 

### 2단계 - 점진적인 리팩터링
- [X] Controller 스캔에 대한 책임을 ControllerScanner로 이동시킨다.
- [X] HandlerMappingRegistry에서 HandlerMapping을 처리하도록 한다.
- [X] HandlerAdapterRegistry에서 HandlerAdapter를 처리하다록 한다.

#### AnnotationHandlerMapping.initialize()의 역할
- @Controller가 붙은 클래스들을 찾아온다.
- 클래스들에서 @RequestMapping이 붙은 메서드들을 찾아와 HandlerKey와 HandlerExecution을 만든다.

### 3단계 - JSON View 구현하기
- [X] JsonView 클래스를 구현한다.
  - [X] JSON으로 응답할 때 ContentType은 MediaType.APPLICATION_JSON_UTF8_VALUE이어야 한다.
  - [X] model에 데이터가 0개면 빈 문자열을 반환한다.
  - [X] model에 데이터가 1개면 값을 그대로 반환한다.
    - [X] key는 반환하지 않는다.
    - [X] 기본타입, 래퍼타입 일 경우에는 JSON으로 변환하지 않고 그 값을 그대로 반환한다.
    - [X] 기본타입, 래퍼타입 이외의 경우에는 JSON으로 변환해서 반환한다.
  - [X] model에 데이터가 2개 이상이면 Map형태 그대로 JSON으로 변환해서 반환한다. 
  - [X] UserController가 JSON 형태로 응답을 반환한다.
- [X] Legacy MVC를 제거한다.
  - [X] 모든 컨트롤러를 어노테이션 기반 MVC로 변경한다.
  - [X] asis 패키지의 모든 클래스를 삭제해도 프로그램이 정상적으로 동작해야한다.
- [ ] HTTP Request Body로 JSON 타입의 데이터를 받았을 때, 이를 자바 객체로 변환한다.
  - [ ] JSON을 자바 객체로 변환할 때 Jackson 라이브러리를 이용한다.
