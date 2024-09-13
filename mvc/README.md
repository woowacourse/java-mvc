# Spring MVC 구현하기

## 1단계: `@MVC` Framework 구현하기
- [ ] 어노테이션 기반으로 동작하는 `AnnotationHandlerMapping` 클래스를 완성한다.
  - [ ] `@Controller`를 가진 클래스를 찾는다.
  - [ ] 그 안에서 `@RequestMapping`을 가진 메서드를 찾아 등록한다.
  - [ ] `HandlerExecution`을 어떻게 사용하는지 파악하고, 이를 구현한다.
  - [ ] `DispatcherServlet`을 보고, `JspView`로 렌더링 부분을 가져온다.
- [ ] 제공된 테스트를 통과한다.
