# 만들면서 배우는 스프링

## @MVC 구현하기

### 학습목표
- @MVC를 구현하면서 MVC 구조와 MVC의 각 역할을 이해한다.
- 새로운 기술을 점진적으로 적용하는 방법을 학습한다.

### 시작 가이드
1. 미션을 시작하기 전에 학습 테스트를 먼저 진행합니다.
    - [Junit3TestRunner](study/src/test/java/reflection/Junit3TestRunner.java)
    - [Junit4TestRunner](study/src/test/java/reflection/Junit4TestRunner.java)
    - [ReflectionTest](study/src/test/java/reflection/ReflectionTest.java)
    - [ReflectionsTest](study/src/test/java/reflection/ReflectionsTest.java)
    - 나머지 학습 테스트는 강의 시간에 풀어봅시다.
2. 학습 테스트를 완료하면 LMS의 1단계 미션부터 진행합니다.

<details>
<summary>Step 1: @MVC 프레임워크 구현 — 기능 구현 목록</summary>

`@Controller`와 `@RequestMapping`을 읽어 요청 주소와 HTTP 메서드에 맞는
컨트롤러 메서드를 찾고 실행한다. 실행 결과의 데이터를 JSP에 전달해 응답한다.
기존 Controller 인터페이스는 유지하고, 두 MVC 방식의 통합은 Step 2에서 진행한다.

각 기능 구현 단위로 커밋할 때 해당 항목을 `- [x]`로 변경한다.

- [x] ControllerScanner: 지정한 패키지에서 `@Controller` 클래스를 찾고 객체를 생성한다.
- [x] AnnotationHandlerMapping 초기화: `@RequestMapping` 메서드를 찾아 URL과 HTTP 메서드별 실행 대상을 등록한다.
- [x] HTTP 메서드 생략 지원: `method`를 지정하지 않으면 지원하는 모든 HTTP 메서드에 등록한다.
- [x] 요청 매핑 조회: 요청의 URL과 HTTP 메서드로 HandlerExecution을 찾는다.
- [x] HandlerExecution: 컨트롤러 객체의 메서드를 `invoke()`로 호출하고 ModelAndView를 반환한다.
- [x] JspView forward: 모델 데이터를 요청 속성에 담고 지정한 JSP로 전달한다.
- [x] JspView redirect: `redirect:` 뒤의 주소로 리다이렉트한다.
- [x] Step 1 테스트: GET·POST 매핑, 같은 URL의 메서드 구분, 메서드 생략, JSP 모델 전달·forward·redirect를 검증한다.

</details>

## 학습 테스트
1. [Reflection API](study/src/test/java/reflection)
2. [Servlet](study/src/test/java/servlet)
