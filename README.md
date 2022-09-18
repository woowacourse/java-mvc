# @MVC 구현하기

## [🚀 1단계 - @MVC 프레임워크 구현하기](https://techcourse.woowahan.com/s/cCM7rQR9/ls/ul3SweFH)

### 기능 요구사항
어노테이션 기반의 MVC 프레임워크를 구현한다.

#### AnnotationHandlerMapping 구현
- [ ] 특정 package 내에서 `@Controller` annotation이 달린 class를 찾는다.
- [ ] controller class 내에서 `@RequestMapping` annotation이 달린 method를 찾는다.
- [ ] `@RequestMapping`에서 지정한 url과 http method에 대해 `HandlerExecution`을 mapping한다.
