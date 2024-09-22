# 1단계 - @MVC 프레임워크 구현하기
- [x] 어노테이션 기반 MVC 프레임 워크로 개선한다.
- [x] URL을 컨트롤러에 매핑하면서 HTTP 메서드(GET, POST, PUT, DELETE 등)도 매핑 조건에 포함한다.
- [x] @RequestMapping()에 method 설정이 되어 있지 않으면 모든 HTTP method를 지원해야 한다.
  - 매핑 조건: HTTP 메서드, URL
## 요구사항
- [x] @MVC Framework 테스트 통과하기
  -  AnnotationHandlerMappingTest 클래스의 테스트를 성공시킨다.
- [x] JspView 클래스를 구현한다.
  - DispatcherServlet 클래스의 service 메서드에서 뷰를 처리하는 로직을 JspView 클래스로 옮긴다.
