# Step 1 - @MVC 프레임워크 구현

## @MVC Framework

- [x] `AnnotationHandlerMappingTest`를 통과한다.
- [x] URL과 HTTP Method를 기준으로 Handler를 매핑한다.
- [x] `@RequestMapping`에 method가 없으면 모든 HTTP Method를 지원한다.

## View

- [ ] `JspView`를 구현한다.
- [ ] `DispatcherServlet`의 뷰 처리 책임을 `JspView`로 이동한다.

## 제약

- [ ] 기존 `Controller` 인터페이스는 변경하지 않는다.
- [ ] 기존 Controller 방식과의 통합은 2단계에서 진행한다.

## 참고사항

- 프레임워크 영역과 서비스 영역을 분리하기 위해 멀티모듈을 적용했다.
- mvc 모듈은 프레임워크, app 모듈은 프로덕션 영역이다.
