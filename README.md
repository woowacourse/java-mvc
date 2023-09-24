# @MVC 구현하기

### 1 단계 - @MVC 프레임워크 구현하기

- [x] AnnotationHandlerMappingTest 클래스의 실패하는 테스트를 통과시킨다.
- [x] Controller 어노테이션이 적용된 클래스를 전부 반환하는 기능을 추가한다.
- [x] 클래스 레벨의 RequestMapping 적용한다.
- [x] GET, POST, PATCH, PUT, DELETE Mapping 추가한다.

### 2단계 - 점진적인 리팩터링

- [x] RegisterController를 어노테이션 기반으로 변경해도 동작하도록 만든다.
- [x] HandlerMapping 인터페이스를 추가한다.
- [x] HandlerMapping의 일급 컬렉션을 추가한다.
- [x] HandleAdapter 인터페이스를 추가한다.
- [x] HandleAdapter의 일급 컬렉션을 추가한다.

### 3단계 - Json View 구현하기

- [x] JspView를 구현한다.
- [x] JsonView를 구현한다.
- [ ] 모든 컨트롤러를 어노테이션 기반으로 변경한다.
- [ ] DispatcherServlet을 MVC 패키지로 이동한다.
