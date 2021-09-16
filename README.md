# MVC 프레임워크 구현하기
## 1단계
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

## 2단계
- [x] 리플렉션 스캐너 명시적으로 기입하기
- [x] given 코드 리팩토링
- [x] AnnotationHandlerMapping 로거 왜 제거했는지 떠올리기
- [x] `orElseThrow()` Throw 할 예외 추가해주기
- [x] Adapter 인터페이스 supports 네이밍 리팩토링
- [x] 테스트 클래스 레벨에도 `@DisplayName` 어노테이션 기입하기
- [x] 일급컬렉션 `findFirst()` 에서 `findAny()`를 사용하도록 리팩토링
- [x] Controller, Service, Repository 인스턴스 싱글톤화

Repository, Service를 주입하기 위해선 AnnotationHandlerMapping에서 
이미 Service, Repotisory를 전달 받아서 Controller 인스턴스를 생성해야 했다.

그러려면 이미 Service, Repostiroy 인스턴스가 생성되어 있어야했는데, 
이 과정에서 Repository의 인스턴스가 생성되게 되면, AnnotationHandlerMapping을 사용하지 않는 기존 
LegacyMVC와 Repository가 달라질 가능성이 존재했다.

<br>

## 3단계
- [x] 로깅 레벨 수정
- [ ] ~~DisplayName 명사형으로~~ Nested를 사용하다보니 동사가 더 자연스러운거 같다!!
- [ ] LegacyMVC 제거
- [ ] Jackson 라이브러리를 이용한 JSON 반환
