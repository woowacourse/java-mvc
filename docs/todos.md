# 1,2 단계

- [x] reflection 학습 테스트
- [x] 어노테이션 학습테스트
- [x] AnnotationHandlerMappingTest 
- [x] 기존 코드에서 register 부분 Controller 어노테이션으로 변환작업
- [x] modelAndView 전환
- [x] 리팩토링

# 3단계
- [ ] DispatcherServlet가 아닌 뷰에서 JSP를 반환하도록 수정
- [ ] HTML 이외에 REST API를 지원할 수 있도록 JSON 뷰를 추가
    - [ ] mvc 모듈의 JspView와 JsonView 클래스를 구현
- [ ] UserController 추가해서 정상동작하는지 확인
- [ ] model에 데이터가 1개면 값을 그대로 반환하고 2개 이상이면 Map 형태 그대로 JSON으로 변환해서 반환
- [ ] Legacy MVC 제거
- [ ] 테스트코드 작성
