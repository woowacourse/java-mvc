# MVC 프레임워크 구현하기
- [x] `AnnotationHandlerMapping.initialize` 메서드 완성
- [x] `AnnotationHandlerMappingTest` 테스트 통과 시키기
- [x] `AnnotationHandlerMapping` 리팩토링
- [x] `DispatcherServlet` 리팩토링
- [x] `AnnotationHandlerAdapter` 클래스 완성
- [ ] Legacy MVC도 Annotation Based MVC와 함께 동작하도록 하기

- DispatcherServlet에선 Controller가 redirect 해야할 경로를 줬건 말건 알바 아니지 않을까?
  - 그건 View의 '정보'를 넘겨 받은 View 단에서 알아서 Model 참고해서 렌더링 해야지?