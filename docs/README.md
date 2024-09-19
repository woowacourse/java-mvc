# 기능 요구 사항

## Step 1

- [x] base package에서 @Controller 어노테이션이 붙은 클래스를 찾고, 해당 클래스의 메서드 중 @RequestMapping 어노테이션이 붙은 메서드를 찾아서 HandlerKey,
  HandlerExecution를 Map에 저장한다.
    - [x] base package 리플렉션 이용
    - [x] @Controller 어노테이션 붙은 클래스 찾기
    - [x] @RequestMapping 어노테이션 붙은 메서드 찾기
    - [x] @RequestMapping()에 method 설정이 되어 있지 않으면 모든 HTTP method를 지원
    - [x] HandlerExecution의 handle 메서드를 실행하면 해당 메서드를 실행하도록 구현
    - [x] AnnotationHandlerMapping의 getHandler에서 uri와 method를 통해 HandlerKey를 생성하고, HandlerExecution을 반환하도록 작성
- [x] AnnotationHandlerMappingTest 테스트 통과
- [x] DispatcherServlet의 View 처리 부분 JspView 부분에 작성

## Step 2

- [x] Manual과 Annotation 방식이 모두 작동하게 DispatcherServlet 수정
  - [x] ManualHandlerMapping과 AnnotationHandlerMapping이 HandlerMapping 인터페이스를 구현하도록 수정
  - [x] ManualHandlerAdapter 구현
  - [x] AnnotationHandlerAdapter 구현
  - [x] DispatcherServlet에서 HandlerMapping과 HandlerAdapter를 사용하도록 수정
- [x] 존재하지 않는 핸들러인 경우 404 에러 페이지로 이동하도록 수정
