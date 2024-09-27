# 기능 목록

## 1. @MVC 프레임워크 구현

- [x] @MVC Framework 테스트 통과(AnnotationHandlerMappingTest)
    - [x] @RequestMapping()에 method 설정이 되어 있지 않으면 모든 HTTP method를 지원한다.
- [x] JspView 클래스를 구현
    - DispatcherServlet 클래스의 service 메서드 중 뷰에 대한 처리 부분을 JspView 클래스로 옮긴다.

## 2. 점진적인 리팩터링

- [x] Legacy MVC와 @MVC 통합
    - 인터페이스 컨트롤러를 어노테이션 기반 컨트롤러로 변경해도 정상 동작한다.
    - [ ] ControllerScanner 클래스 추가
        - 컨트롤러를 찾아서 인스턴스 생성하는 역할
        - Relfections 라이브러리를 사용한다.
        - Relfections 객체로 @Controller가 설정된 모든 클래스를 찾는다.
        - 각 클래스의 인스턴스를 생성한다.
    - [ ] HandlerMapping 인터페이스 분리
        - DispatcherServlet의 초기화 과정에서 ManualHandlerMapping, AnnotationHandlerMapping을 모두 초기화
        - 초기화한 2개의 HandlerMapping List로 관리한다.
    - [ ] HandlerAdapter 인터페이스 분리
        - HandlerMapping 클래스에서 찾은 컨트롤러를 실행하는 역할
