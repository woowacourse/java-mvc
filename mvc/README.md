# Spring MVC 프레임워크 만들기

## 1단계 - @MVC 프레임워크 구현하기

- [x] Annotation 기반 HandlerMapping Entry를 초기화한다.
    - [x] 리플렉션을 활용하여 @Controller를 사용한 클래스 정보를 읽어온다.
    - [x] 리플렉션을 활용하여 @RequestMapping을 사용한 핸들러 정보를 읽어온다.
        - [x] @RequestMapping에 HttpMethod가 지정되지 않을 경우 모든 HttpMethod를 지원해야 한다.
    - [x] 핸들러를 식별하는 키와 핸들러 실행 정보를 Entry에 추가한다.
- [x] JspView 클래스를 구현한다.
    - [x] DispatcherServlet 클래스의 View 처리 로직을 JspView 클래스로 분리한다.

## 2단계 - 점진적인 리팩토링

- [x] 프레임워크가 다양한 컨트롤러를 지원할 수 있도록 어댑터를 추가한다.
    - [x] Controller Interface 기반의 핸들러를 지원하는 어댑터를 구현한다.
    - [x] 어노테이션 기반의 핸들러를 지원하는 어댑터를 구현한다.
    - [x] Dispatcher Servlet에서 핸들러와 어댑터를 조회하고 어댑터를 실행한다.

## 3단계 - Json View 구현하기

- [x] HTML 이외에 JSON으로도 응답할 수 있도록 JsonView 클래스를 구현한다.
- [ ] Legacy MVC를 제거한다.
    - [ ] 모든 컨트롤러를 어노테이션 기반 MVC로 변경한다.
    - [ ] 레거시 코드를 삭제해도 서비스가 정상 동작하도록 리팩토링한다.
