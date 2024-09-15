# Spring MVC 구현하기

## 1단계: `@MVC` Framework 구현하기
- [x] 어노테이션 기반으로 동작하는 `AnnotationHandlerMapping` 클래스를 완성한다.
  - [x] `@Controller`를 가진 클래스를 찾는다.
  - [x] 그 안에서 `@RequestMapping`을 가진 메서드를 찾아 등록한다.
    - [ ] value가 없는 경우, 모든 RequestMethod에 대해 등록한다.
  - [x] `HandlerExecution`을 어떻게 사용하는지 파악하고, 이를 구현한다.
    - 메서드를 받아 선언한 클래스를 객체화한 뒤, 메서드를 호출한다.
    - 싱글톤으로 관리할 수 없을까?
  - [x] `DispatcherServlet`을 보고, `JspView`로 렌더링 부분을 가져온다.
- [x] 제공된 테스트를 통과한다.
- [x] 핸들러를 제공한 클래스를 싱글톤으로 관리한다.
