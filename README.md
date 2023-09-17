# @MVC 구현하기

## 기능 요구 사항

### 1 단계 : @MVC Framework

- [x] AnnotationHandlerMappingTest 성공하기
  - [x] Map<HandlerKey, HandlerExecution> handlerExecutions 에 TestController 메서드 추가하기
  - [x] HandlerExecution 에서 method.invoke() 호출

### 2 단계 : Legacy MVC와 @MVC 통합
![diagram.png](diagram.png)

- [ ] HandlerMapping 추가
  - [ ] AnnotationHandlerMapping, ManualHandlerMapping 추상화
  - [ ] DispatcherServlet 의존성 변경

- [ ] Adaptor 추가
  - [ ] Controller, HandlerExecution 연결

