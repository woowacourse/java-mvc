# @MVC 프레임워크 구현

# 1단계

## 요구사항

- [x] AnnotationHandlerMappingTest가 정상 동작한다.
- [x] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여 AnnotationHandlerMapping과 ManualHandlerMapping 둘다 처리할 수 있다.

# 2단계

## 요구사항

- [x] `PeanutBox` (**땅콩박스**)에서 `@Controller`가 붙은 클래스를 찾을 수 있다.
- [x] `HandlerMappingRegistry` 클래스에서 `HandlerMapping`을 처리하도록 구현했다.
- [x] `HandlerAdapterRegistry` 클래스에서 `HandlerAdapter`를 처리하도록 구현했다.

# 3단계

## 요구사항

- [x] JspView 클래스를 구현한다.
- [x] JsonView 클래스를 구현한다.
- [ ] Legacy MVC 제거하기
  - 하지만 필자는 제거하지 않으려 한다...
- [ ] 힌트에서 제공한 UserController 컨트롤러가 json 형태로 응답을 반환한다.
- [ ] 레거시 코드를 삭제하고 서버를 띄워도 정상 동작한다.

# 홀로 만들어보기

## 요구사항

### DI Container
- [x] `Peanut`이 담긴 `DI Container`를 만든다
- [x] Controller -> Service -> Repository와 같이 주입이 가능하도록 만든다
  - [x] 가급적 생성자 주입 방식으로 만든다
  - [x] 스프링이 생성해주는 것과 같이 DFS로 생성하도록 한다
  - [x] `InMemoryRepository` 클래스를 `static 유틸 클래스`에서 `Peanut` 등록된 객체로 만든다

### @Toransactional

- [ ] `@Transactional`과 같이 어노테이션 기반의 프록시 객체를 만들어서 작업의 원자성을 보장하는 기능을 만든다.
  - Spring을 사용하는 것은 반칙!! 대신 `CGLIB`를 이용한다
    - 프록시 객체를 동적으로 생성한다
  - `BeanPostProcessor`의 아류로 `PeanutPostProcessor`를 만들어서 Peanut을 가로챈다
