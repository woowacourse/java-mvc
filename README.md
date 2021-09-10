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
