# @MVC 구현하기 - step3

## 이전 구현

[톰캣 구현하기🐱](https://github.com/BETTERFUTURE4/jwp-dashboard-http)

## 기능 요구 사항

## 미션 설명

현재는 DispatcherServlet에서 JSP를 직접 처리하고 있다.
화면에 대한 책임은 View 클래스가 갖도록 만들자.
DispatcherServlet이 아닌 뷰에서 JSP를 반환하도록 수정하자.
그리고 REST API를 지원할 수 있도록 Json 뷰를 추가하자.

### JspView 클래스를 구현한다.

nextstep.jwp.mvc.view 패키지에서 JspView 클래스를 찾을 수 있다.
DispatcherServlet 클래스의 service 메서드에서 어떤 부분이 뷰에 대한 처리를 하고 있는지 파악해서 JspView 클래스로 옮겨보자.


### JsonView 클래스를 구현한다.

nextstep.jwp.mvc.view 패키지에서 JsonView 클래스를 찾을 수 있다.
HTTP Request Body로 JSON 타입의 데이터를 받았을 때 어떻게 자바에서 처리할지 고민해보고 JsonView 클래스를 구현해보자.

## Legacy MVC 제거하기

app 모듈에 있는 모든 컨트롤러를 어노테이션 기반 MVC로 변경한다.
그리고 asis 패키지에 있는 레거시 코드를 삭제해도 서비스가 정상 동작하도록 리팩터링하자.

## 체크리스트

- [x] 힌트에서 제공한 UserController 컨트롤러가 json 형태로 응답을 반환한다.
- [x] 레거시 코드를 삭제하고 서버를 띄워도 정상 동작한다.

## 피드백

- [x] Json 파싱 전략 수정
- [x] 디스패처 서블릿 render 메소드 복구
