# MVC 프레임워크 구현하기

## 💪 구현할 기능 목록
- [x] 학습테스트 진행/공부
    - [x] Reflections
    - [x] AnnotationHandlerMappingTest
    - [x] ServletTest

- [x] 전반적인 서비스 플로우 공부하기 
    - https://github.com/PapimonLikelion/woowacourse-TIL/blob/master/Level4/2021-09-09-mvc-mission.md

- [x] 어노테이션 기반의 핸들러 매핑 구현
    - [x] AnnotationHandlerMapping 구현
    - [x] AppWebApplicationInitializer에 해당 핸들러 매핑 추가

- [x] 디스패처 서블릿에서 AnnotationHandlerMapping을 사용하여 요청 처리해주기
    - [x] 컨트롤러의 응답을 JspView에서 어떻게 Response로 반환할지 고민해보기
    - [x] 기존의 레거시 코드를 어노테이션 기반으로 수정해보기
    - [x] 어떤 핸들러 매핑을 우선 순위에 둘지 고민해보기
        - [x] 레거시인 ManualHandlerMapping을 먼저 등록하기로 결정

- [x] HandlerAdapter 적용
    - [x] HandlerMapping에서 getHandler 메서드로 Controller나 HandlerExecutor 가져오기
    - [x] 해당 Handler 처리를 지원하는지를 검사하기
    - [x] 해당 Handler 처리하여 ModelAndView 반환
    - [x] DispatcherServlet에서 ModelAndView 처리

- [x] Legacy MVC 제거하기

- [x] Jackson 라이브러리를 활용한 JsonView 구현
    - [x] DispatcherServlet이 아닌 View에서 JSP 반환하도록 수정

## 📜 리팩터링 목록
- [x] 없는 URL에 대한 요청을 404로 반환하기
- [x] 내부 로직에 오류 발생 시 500을 반환하기
- [x] 테스트 system.out 수정
- [ ] 인스턴스 변수 this 활용에 기준 잡기
- [x] 에러 처리에 있어 jspView 중복 제거
- [ ] 안 쓰이는 레거시 코드 완벽 제거
- [ ] JsonView에서 여러 파라미터의 요청에 대해 응답 받아볼 수 있는 컨트롤러 메서드 추가
