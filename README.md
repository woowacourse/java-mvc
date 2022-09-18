# @MVC 구현하기

- [X] 어노테이션 기반의 MVC 프레임워크로 개선
- [X] URL을 컨트롤러에 매핑하면서 HTTP 메서드(GET, POST, PUT, DELETE 등)도 매핑 조건에 포함시킨다.
- [X] handlerMapping, handlerAdapter 구현
  - handelrMapping에서 handler를 찾고,
  - 위에서 찾은 handler를 실행시킬 수 있는 handlerAdapter를 찾는다.
    - handlerAdapter가 view를 반환한다.
  - 반환된 view를 forward()하거나 sendRedirect()한다.