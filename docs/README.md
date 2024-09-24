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

## Step 3

- [x] JsonView 구현
  - [x] Jackson 라이브러리를 사용하여 자바 객체로 변환
  - [x] ContentType은 MediaType.APPLICATION_JSON_UTF8_VALUE로 반환
  - [x] model에 데이터가 1개면 값을 그대로 반환하고 2개 이상이면 Map 형태 그대로 JSON으로 변환해서 반환 ex) 하나면 `{"name": "keesun"}`, 둘 이상이면 `{"user1": {"name": "keesun"}, "user2": {"name": "whiteship"}}`
- [x] app 모듈에 있는 모든 컨트롤러를 어노테이션 기반 MVC로 변경
  - [x] app 모듈에 있는 모든 컨트롤러에 @Controller 어노테이션 추가
- [x] DispatcherServlet을 mvc 패키지로 이동
- [x] mappings, adapters를 dispatcher servlet에서 등록하게 수정
