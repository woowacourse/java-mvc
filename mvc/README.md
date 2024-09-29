# Spring MVC 구현하기

## 1단계: `@MVC` Framework 구현하기
- [x] 어노테이션 기반으로 동작하는 `AnnotationHandlerMapping` 클래스를 완성한다.
  - [x] `@Controller`를 가진 클래스를 찾는다.
  - [x] 그 안에서 `@RequestMapping`을 가진 메서드를 찾아 등록한다.
    - [x] value가 없는 경우, 모든 RequestMethod에 대해 등록한다.
  - [x] `HandlerExecution`을 어떻게 사용하는지 파악하고, 이를 구현한다.
    - 메서드를 받아 선언한 클래스를 객체화한 뒤, 메서드를 호출한다.
    - 싱글톤으로 관리할 수 없을까?
  - [x] `DispatcherServlet`을 보고, `JspView`로 렌더링 부분을 가져온다.
- [x] 제공된 테스트를 통과한다.
- [x] 핸들러를 제공한 클래스를 싱글톤으로 관리한다.

## 2단계: `@MVC` 기반 매핑과 기존 Controller 매핑이 공존하도록 만들기

현재 상황: `Controller`를 상속받고 있고, `ManualHandlerMapping`에서 하나씩 추가해주고 있다.<br/>
`@MVC`에서 어노테이션 기반 (`AnnotationHandlerMapping`)으로 핸들러를 추가하고 매핑할 수 있도록 구현했다.

`Controller`는 `String`을 리턴하고, 어노테이션 기반 메서드는 `ModelAndView`를 리턴해야 하니 간극이 생긴다.

- [x] `ManualHandlerMapping`과 `AnnotationHandlerMapping`을 모두 고려할 수 있도록 추상화한다.
- [x] `RegisterController`를 어노테이션 기반으로 수정한다. (작동하지는 않는다)
- [x] `RegisterController`가 어노테이션 기반으로 작동하도록 구조를 변경한다.
  - [x] Package 기반으로 스캔한다.

`DispatcherServlet`에서 어노테이션 기반, 매뉴얼 기반 핸들러를 모두 사용할 수 있도록 `HandlerMappingRegistry`를 만들었다. 이제 `HandlerMapping`을 상속받는 모든 매핑을 넣을 수 있게 되었다!

하지만 이를 `DispatcherServlet`에 넣는 순간, `@Controller` 어노테이션 기반과 `Controller` 상속 기반에서 메서드 실행에 문제를 겪게 되었다. 메서드를 실행할 때에도 같은 방식으로 추상화하면 어떨까?
- [x] `HandlerAdapter`를 생성하고, 이를 각각 `ControllerHandlerAdapter`와 `HandlerExecutionHandlerAdapter`로 구현한다.
