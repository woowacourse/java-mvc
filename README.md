# @MVC 구현하기

# 1. MVC 프레임워크 구현하기
## 요구사항을 만족시키기 위해 필요한 기능
- [x] 어노테이션 기반으로 핸들러 매핑을 할 수 있다.
  - [x] URL과 HTTP 메소드를 핸들러 매핑 조건에 포함한다.

# 2. Legacy MVC와 @MVC 통합하기
- [x] 컨트롤러 인터페이스 기반 MVC 프레임워크와 @MVC 프레임워크가 함께 사용될 수 있다.
  - [x] 컨트롤러의 수정 없이 기능을 구현한다.

# 3. JSON View 구현하기
- [x] JspView를 구현한다.
- [ ] JsonView를 구현한다.
  - [ ] Json 데이터 타입은 key: value로 구성되어있다.
  - [ ] Json 데이터의 value에는 `key: value` 형태의 값이 들어갈 수 있다.
- [ ] Legacy MVC 제거하기
  - [ ] DispatcherServlet을 mvc 패키지로 옮기기
