# @MVC 구현하기

<details>
<summary>1단계 - @MVC 프레임워크 구현하기</summary>

구현 기능 목록

- [x] AnnotationHandlerMappingTest 통과 시키기
- [x] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여 모든 HandlerMapping 구현체 처리하기

리팩터링

- 테스트 코드 작성
  - [x] 어노테이션 기반으로 잘 동작하는지도 테스트하기
  - [x] 리다이렉트 요청을 잘 처리하고 있는지 확인하기
  - [x] 예외 상황에 대한 테스트 추가

</details>

<details>
<summary>2단계 - 점진적인 리팩터링</summary>

구현 기능 목록

- [ ] ControllerScanner 클래스에서 @Controller가 붙은 클래스를 찾기
- [ ] HandlerMappingRegistry 클래스에서 HandlerMapping을 처리
- [ ] HandlerAdapterRegistry 클래스에서 HandlerAdapter를 처리

</details>
