# @MVC 구현하기

## 1단계 요구사항 도출

- [ ] AnnotationHandlerMappingTest 를 통과 시킨다.
    - [ ] AnnotationHandlerMapping 의 initialize 를 Controller 클래스의 @RequestMapping 이 있는 메서드 경로를 셋팅한다.
    - [ ] getHandler 에서 HandlerExecution 을 꺼내준다.

- [ ] app -> manualHandlerMapping, AnnotationHandlerMapping 을 둘 다 사용할 수 있게 구현을 변경한다.
    - [ ] AppWebApplicationInitializer 에서 Handler 들을 등록시킨다.
    - [ ] 어떤 Handler 가 들어올지 모르기 때문에 핸들러에 맞는 시그니처를 실행시키기 위한 Adapter 를 구현한다.
    - [ ]  manualHandlerMapping 과 AnnotationHandlerMapping 의 반환 값을 ModelAndView 로 통일 시킨다.
