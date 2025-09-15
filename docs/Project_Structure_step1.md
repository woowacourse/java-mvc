## @MVC Framework 테스트 통과하기

### AnnotationHandlerMapping 구조

- 주어진 basePackage 내부에 있는 모든 컨트롤러 탐색 (@Controller가 적용된 클래스 탐색)
- 각 컨트롤러마다의 API 메서드를 탐색 (@RequestMapping이 적용된 메서드 탐색)
- API 메서드 단위로 HandlerExecution를 만들어 등록

### HandlerExecution 구조

- HandlerExecution는 자체적으로 Controller 인스턴스와 Method를 필드로 보유
- handle() 메서드 호출시 인자인 request, response를 통해 Method 실행

## JspView 클래스를 구현하기

### JspView의 구조

- viewName을 생성 인자로 받아 필드로 저장
- render() 메서드를 통해 클라이언트에게 전달할 응답 데이터를 구성
    - viewName이 REDIRECT_PREFIX("redirect:")로 시작할 경우
      -> 단순히 리다이렉션 응답 처리
    - viewName이 REDIRECT_PREFIX("redirect:")로 시작하지 않을 경우
      -> 요청에 모델 정보를 담아 JSP 파일로 포워딩
      -> JSP 파일에서는 요청의 모델 정보를 참조해 동적 페이지를 랜더링
      -> 완성된 동적 페이지를 응답 객체 등록
