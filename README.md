# @MVC 구현하기
## 🚀 About the Mission
이전 미션에서 HTTP 서버를 만들고 Controller 인터페이스를 활용해 MVC 프레임워크를 구현했다.
이번에는 개발자가 비즈니스 로직 구현에만 집중 할 수 있도록 Annotation 기반의 @MVC 프레임워크를 직접 구현해보면서 점진적인 리팩토링을 경험한다.

<br>

## 📌 Step 1 - @MVC 프레임워크 구현
- 📝 <b>요구사항</b>
  - Annotation 기반의 @MVC 프레임워크 구현하기
- 🖊 <b>구현 목록</b>
  - AnnotationHandlerMapping 클래스 작성
  - DispatcherServlet에 있는 View render 로직을 JspView 클래스에 위임
  - Controller 간의 호환성을 위한 HandlerAdapter 구현
  - 구현한 로직에 대한 검증 테스트 작성
- ✅ <b>체크 리스트</b>
  - [x] AnnotationHandlerMappingTest가 정상 동작한다.
  - [x] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여 AnnotationHandlerMapping과 ManualHandlerMapping 둘다 처리할 수 있다.

<br>

## 📌 Step 2 - 점진적인 리팩토링
- 📝 <b>요구사항</b>
  - Legacy MVC와 @MVC 통합하기
- 🖊 <b>구현 목록</b>
  - ControllerScanner 클래스 구현
  - HandlerMappingRegistry 클래스 구현
  - HandlerAdapterRegistry 클래스 구현
  - 구현한 로직에 대한 검증 테스트 작성
- ✅ <b>체크 리스트</b>
  - [x] 기존의 로그인 컨트롤러를 어노테이션 기반 컨트롤러로 변경해도 정상 동작한다.
  - [x] ControllerScanner 클래스에서 @Controller가 붙은 클래스를 찾을 수 있다.
    - [x] Controller 클래스와 인스턴스를 Map 형태로 반환할 수 있다.
  - [x]  HandlerMappingRegistry 클래스에서 HandlerMapping을 처리하도록 구현했다.
  - [x]  HandlerAdapterRegistry 클래스에서 HandlerAdapter를 처리하도록 구현했다.

<br>

## 📌 Step 3 - JSON View 구현하기
- 📝 <b>요구사항</b>
  - JspView 클래스 구현
  - JsonView 클래스 구현
  - Legacy MVC 제거
- 🖊 <b>구현 목록</b>
  - ObjectMapper를 이용한 JsonView render 로직 구현
  - Legacy 코드 리팩토링 / 삭제
  - 구현한 로직에 대한 검증 테스트 작성
  - 리팩토링한 app 모듈 컨트롤러 정상 동작 검증 테스트 작성
- ✅ <b>체크 리스트</b>
  - [x] UserController 컨트롤러가 json 형태로 응답을 반환한다.
  - [x] 레거시 코드를 삭제하고 서버를 띄워도 정상 동작한다.