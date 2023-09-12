# @MVC 구현하기

### 1 단계 - @MVC 프레임워크 구현하기

- [x] AnnotationHandlerMappingTest 클래스의 실패하는 테스트를 통과시키기

### 2단계 - 점진적인 리팩터링

- [ ] RegisterController를 어노테이션 기반으로 변경해도 동작하도록 만들기
- [x] 컨트롤러를 찾아서 인스턴스 생성하는 역할을 담당하는 ControllerScanner 추가
- [x] 기존의 AnnotationHandlerMapping 클래스에 ControllerScanner 사용하도록 적용
- [x] HandlerMapping 인터페이스 추가
- [x] HandlerMapping 리스트를 가지고 있는 HandlerMappingRegistry 추가
- [x] HandlerExecution을 실행시키는 HandlerExecutorHandlerAdapter 추가
