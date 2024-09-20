# 1단계 요구사항
- [x] @MVC Framework 테스트 통과하기
  - URL을 컨트롤러에 매핑하면서 HTTP 메서드(GET, POST, PUT, DELETE 등)도 매핑 조건에 포함시킨다.
  - `@RequestMapping()`에 method 설정이 되어 있지 않으면 모든 HTTP method를 지원한다.
- [x] JspView 클래스 구현하기
  - DispatcherServlet 클래스의 service 메서드에서 어떤 부분이 뷰에 대한 처리를 하고 있는지 파악해서 JspView 클래스로 옮긴다.
