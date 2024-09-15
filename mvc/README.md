# Spring MVC 프레임워크 만들기

## 1단계 - @MVC 프레임워크 구현하기

- [x] Annotation 기반 HandlerMapping Entry를 초기화한다.
    - [x] 리플렉션을 활용하여 @Controller를 사용한 클래스 정보를 읽어온다.
    - [x] 리플렉션을 활용하여 @RequestMapping을 사용한 핸들러 정보를 읽어온다.
    - [x] 핸들러를 식별하는 키와 핸들러 실행 정보를 Entry에 추가한다.
- [ ] JspView 클래스를 구현한다.
    - [ ] DispatcherServlet 클래스의 View 처리 로직을 JspView 클래스로 분리한다.
