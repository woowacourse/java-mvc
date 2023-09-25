# @MVC 구현하기

### 1단계 - @MVC 프레임워크 구현하기
- [x] HTTP 메서드와 URL를 매핑 조건으로 만들기
- [x] AnnotationHandlerMappingTest 클래스의 실패하는 테스트를 통과

### 2단계 -  점진적인 리팩터링
- [x] Legacy MVC와 @MVC 통합하기
  - [x] 컨트롤러 인터페이스 기반 MVC 프레임워크와 @MVC 프레임워크가 공존

### 3단계 - JSON View 구현하기
- [x] 화면에 대한 책임은 View 클래스가 갖도록 하기
  - [x] JspView 클래스를 구현한다
  - [x] JsonView 클래스를 구현한다
    -  [x] REST API를 지원
- [x] Legacy MVC 제거하기
  - [x] app 모듈에 있는 모든 컨트롤러를 어노테이션 기반 MVC로 변경
  - [x] asis 패키지에 있는 레거시 코드를 삭제
  - [x] DispatcherServlet도 app 패키지가 아닌 mvc 패키지로 옮기기
