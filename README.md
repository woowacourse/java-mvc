# @MVC 구현하기

## 기존 코드 흐름 이해하기

- `Tomcat`에 `DispatcherServlet`를 등록한다.
    - `DispatcherServlet`은 `ManualHandlerMapping`을 가지고 있고, 이를 이용해 응답을 처리한다.
    - `ManualHandlerMapping`에는 개발자가 작성한 `Controller`를 `Path`와 매핑할 수 있다.
    - init() 메서드로 초기화해주어야 사용할 수 있다.
    - `DispatcherServletInitializer`로 DispatcherServlet 초기화헤야 한다.
- `DispatcherServletInitializer`의 `onStartUp`을 실행한다.
    - `DispatcherServletInitializer`는 `WebApplicationInitializer` 인터페이스를 구현한다.
    - `SpringServletContainerInitializer`에서 Reflection 으로 이 클래스를 찾아 해당 메서드를 실행한다.
    - `ServletContext`에 `DispatcherServlet`을 등록한다. 아마 여기서 init()을 해줄 것이다.
    - 아직 정확히 뭔지는 모르겠지만 ...`Registeration` 설정을 해준다.
        - `DispatcherServlet`의 `loadOnStartUp` 값 설정,  "/"에 매핑

- 기존 코드의 Legacy MVC는 Path와 Controller는 매핑되어있지만, HttpMethod와는 매핑되어있지 않다.
    - 그래서 /login, /login/view로 구분되어 있다.

## 요구사항

### 1단계 @MVC 프레임워크 구현하기

- [x] @RequestMapping 어노테이션으로 URL 및 메서드를 컨트롤러에 매핑하기
    - [x] `AnnotationHandlerMapping`에서 어노테이션에 따른 매핑을 구현한다. (초기화)
    - [x] `HandlerExecution`의 `handle` 메서드를 구현한다.
    - [x] 매핑 시 발생하는 예외를 처리한다.

## 2단계 점진적인 리팩터링

- [x] Legacy MVC와 @MVC 통합하기
    - [x] 실제로 `ManualHandlerMapping`과 `AnnotationHandlerMapping` 클래스를 모두 사용하게 만든다.
    - [x] 컨트롤러 인터페이스 기반 MVC 프레임워크와 @MVC 프레임워크가 공존하도록 만든다.
        - [x] 동작 확인을 위해 회원가입 컨트롤러를 어노테이션 기반 컨트롤러로 변경한다.
        - [x] `DispatcherServlet`이 `AnnotationHandlerMapping`, `ManualHandlerMapping`을 모두 관리하도록 한다.
        - [x] `AnnotationHandlerMapping`을 먼저 확인한 뒤, 해당하는 컨트롤러가 없으면 `ManualHandlerMapping`을 찾도록 한다.
        - [x] `ModelAndView`에서 사용하는 `JspView` 를 동작하게 만든다.
    - 리팩터링
        - 패키지에 따른 설계가 적절한지 확인한다.
        - 변화에 유연하도록 추상화한다.
            - DispatcherServlet에 두 가지 경우에 대한 단순 분기만 만들면, 이후 변화에도 유연할 수 없다.
            - HandlerMapping을 추상화하고, 같은 형식으로 handle할 수 있게 한다.
        - 예외에 대해 원하는 화면을 내려주도록 app 패키지 영역에서 HandlerMapping을 추가한다.
