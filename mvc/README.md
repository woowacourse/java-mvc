
## 기능 구현 목록

### 1단계 - @MVC 프레임워크 구현하기
- [x] @MVC Framework 테스트 통과하기
    - [x] AnnotationHandlerMapping.initialize() 및 getHandler() 구현
    - [x] HandlerExecution.handle() 구현
- [x] JspView 클래스 구현

### 2단계 - 점진적인 리팩터링

- [x] ControllerScanner 객체를 통해 컨트롤러 인스턴스 생성하는 로직 분리
    - [x] AnnotationHandlerMapping 리팩터링
- [x] Legacy MVC 와 @MVC 통합하기
    - [x] HandlerMapping 인터페이스로 AnnotationHandlerMapping 과 ManualHandlerMapping 통합
    - [x] HandlerAdaptor 인터페이스로 Handler 을 통한 메서드 실행
    - [x] 각 객체를 관리하는 registry 구현
    - [x] DispatcherServlet 리팩터링

### 3단계 - JSON View 구현하기

- [ ] JsonView 구현
- [ ] Legacy MVC 제거
  - [ ] app 모듈 내 모든 컨트롤러 어노테이션 기반으로 수정
  - [ ] as-is 패키지 삭제
  - [ ] DispatcherServlet mvc 패키지로 이동
