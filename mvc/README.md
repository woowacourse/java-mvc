# 기능 목록

## 1. @MVC 프레임워크 구현

- [x] @MVC Framework 테스트 통과(AnnotationHandlerMappingTest)
    - [x] @RequestMapping()에 method 설정이 되어 있지 않으면 모든 HTTP method를 지원한다.
- [x] JspView 클래스를 구현
    - DispatcherServlet 클래스의 service 메서드 중 뷰에 대한 처리 부분을 JspView 클래스로 옮긴다.

## 2. 점진적인 리팩터링

- [x] Legacy MVC와 @MVC 통합
    - 인터페이스 컨트롤러를 어노테이션 기반 컨트롤러로 변경해도 정상 동작한다.
    - [x] ControllerScanner 클래스 추가
        - 컨트롤러를 찾아서 인스턴스 생성하는 역할
        - Relfections 라이브러리를 사용한다.
        - Relfections 객체로 @Controller가 설정된 모든 클래스를 찾는다.
        - 각 클래스의 인스턴스를 생성한다.
    - [x] HandlerMapping 인터페이스 분리
        - DispatcherServlet의 초기화 과정에서 ManualHandlerMapping, AnnotationHandlerMapping을 모두 초기화
        - 초기화한 2개의 HandlerMapping List로 관리한다.
    - [x] HandlerAdapter 인터페이스 분리
        - HandlerMapping 클래스에서 찾은 컨트롤러를 실행하는 역할

## 3. JSON View 구현

- [x] JsonView 클래스 구현
    - JSON 자바 객체 변환 시, Jackson 라이브러리를 사용한다.
    - JSON으로 응답 시, `ContentType` -> `MediaType.APPLICATION_JSON_UTF8_VALUE`으로 변환한다.
    - model 데이터 개수 1개면 값 그대로 반환, 2개 이상이면 Map 형태 그대로 JSON으로 변환해서 반환한다.
- [ ] Legacy MVC 제거
    - [ ] app 모듈에 있는 모든 컨트롤러를 어노테이션 기반 MVC로 변경한다.
    - [ ] asis 패키지에 있는 레거시 코드를 삭제해도 서비스가 정상 동작한다.
    - [ ] DispatcherServlet를 mvc 패키지로 옮긴다.
