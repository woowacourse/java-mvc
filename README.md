# @MVC 프레임워크 구현

# 미션 요구사항

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
- [x] 힌트에서 제공한 UserController 컨트롤러가 json 형태로 응답을 반환한다.
- Legacy MVC 제거하지 않습니다
  - 기존의 Legacy코드 또한 잘 돌아가는 것을 확인하기 위하여 제거하지 않습니다.
    - [x] asis `ManualHandlerAdapter` 와 `ManualHandlerMapper` 또한 테스트코드에서가 아닌 프로덕션에서 정상 동작하는 것을 확인합니다.  

# 그외 추가 사항

- 미션 이외에 개인적으로 도전해본 요구 사항입니다.

## DI Container
- [x] `Peanut`이 담긴 `DI Container`를 만든다
- [x] `Controller` -> `Service` -> `Repository`와 같이 주입이 가능하도록 만든다
  - [x] 가급적 생성자 주입 방식으로 만든다
  - [x] 스프링이 생성해주는 것과 같이 `DFS`로 생성하도록 한다
  - [x] `InMemoryRepository` 클래스를 `static 유틸 클래스`에서 `Peanut` 등록된 객체로 만든다
- [x] 수등 등록을 하는 `@PeanutConfiguration`와 `@ThisIsPeanut`을 제공합니다

## @Toransactional

- [x] `@Transactional`과 같이 어노테이션 기반의 프록시 객체를 만들어서 작업의 원자성을 보장하는 기능을 만든다
  - Spring의 코어 라이브러리를 사용하지 않고 `CGLIB`를 이용한다
    - 프록시 객체를 동적으로 생성한다
  - 스프링에서 `BeanPostProcessor`의 기능을 대신 하는 `PeanutPostProcessor`를 만들어서 `Peanut`을 가로챈다
