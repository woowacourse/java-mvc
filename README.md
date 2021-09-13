# MVC 프레임워크 구현하기
- [x] `AnnotationHandlerMapping.initialize` 메서드 완성
- [x] `AnnotationHandlerMappingTest` 테스트 통과 시키기
- [x] `AnnotationHandlerMapping` 리팩토링
- [x] `DispatcherServlet` 리팩토링
- [x] `AnnotationHandlerAdapter` 클래스 완성
- [x] Legacy MVC도 Annotation Based MVC와 함께 동작하도록 하기
- [ ] 직접 짠 코드는 테스트 코드를 짜보자!!!
  - [x] ManualHandlerAdapter
  - [x] AnnotationHandlerAdapter
  - [x] AnnotationHandlerMapping
  - [x] HandlerExecution
  - [x] HandlerExecutions
  - [x] HandlerKey
  - [ ] ~~JspView~~ 도저히 어떻게 테스트를 작성할지 모르겠다...!
  - [x] ModelAndView
  - [ ] ~~DispatcherServlet~~ 역시나 어떻게 테스트를 작성할지 모르겠다...!
  - [x] HandlerAdapters
  - [x] HandlerMappings

- DispatcherServlet에선 Controller가 redirect 해야할 경로를 줬건 말건 알바 아니지 않을까?
  - 그건 View의 '정보'를 넘겨 받은 View 단에서 알아서 Model 참고해서 렌더링 해야지?

<br>

- [x] 리플렉션 스캐너 명시적으로 기입하기
- [ ] given 코드 리팩토링
- [x] AnnotationHandlerMapping 로거 왜 제거했는지 떠올리기
- [ ] `orElseThrow()` Throw 할 예외 추가해주기
- [x] Adapter 인터페이스 supports 네이밍 리팩토링
- [x] 테스트 클래스 레벨에도 `@DisplayName` 어노테이션 기입하기
- [x] 일급컬렉션 `findFirst()` 에서 `findAny()`를 사용하도록 리팩토링
