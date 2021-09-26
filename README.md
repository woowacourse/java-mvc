# MVC 프레임워크 구현하기

## step1
- [x] 학습 테스트 완료
- [x] 어노테이션 기반 리퀘스트 매핑
    - [x] 컨트롤러, 핸들러 메서드 찾기
    - [x] AnnotationHandlerMapping#getHandler -> HandlerExecution 반환
    - [x] AnnotationHandlerMappingTest 통과
    - [x] initialize로 모든 HandlerExecution 초기화 하기
        - [x] ControllerScanner 생성
- [x] Legacy MVC와 Annotation Based MVC 통합
    - [x] JspView 구현

## step2
- [x] DispatcherServlet 리팩토링
  - [x] HandlerAdapter 구현
- [x] AnnotationHandlerMapping 리팩토링
  - [x] ControllerScanner 구현

## step3
- [x] View 구현하기
  - [x] JsonView 구현하기
    - [x] JSON을 자바 객체로 변환할 때 Jackson 라이브러리를 사용한다.
    - [x] JSON으로 응답할 때 ContentType은 MediaType.APPLICATION_JSON_UTF8_VALUE으로 반환해야 한다.
    - [x] model에 데이터가 1개면 값을 그대로 반환하고 2개 이상이면 Map 형태 그대로 JSON으로 변환해서 반환한다.
- [x] Legacy MVC 제거하기
  - [x] Legacy 컨트롤러 -> 어노테이션 기반 컨트롤러로 변경
  - [x] asis 패키지에 있는 레거시 코드를 삭제해도 서비스가 정상 동작하도록 구현
