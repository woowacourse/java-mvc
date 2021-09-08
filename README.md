# MVC 프레임워크 구현하기

1. 학습테스트 통과시키기
    - Junit3TestRunner
    - Junit4TestRunner
    - ReflectionsTest
    - ReflectionTest
    - AnnotationHandlerMappingTest

2. JspView 구현
3. ManualHandlerMapping.java 와 AnnotationHandlerMapping.java 모두 사용할 수 있도록 변경
4. `app/webapp/WEB-INF/web.xml` 에 정적 리소스 매핑 추가
5. Legacy MVC와 Annotation Based MVC 통합 - RegisterController

### 구구 코드리뷰
1. HandlerAdapter 적용, move 메서드 삭제 및 JspView 활용

### 조앤 코드리뷰 - 1차
1. 회원가입 시 로그인 상태가 되는 기능 추가
2. setAccessible(true) -> trySetAccessible() 로 변경
3. HandlerAdapter 외부에서 주입하도록 수정
4. HandlerMappings, HandlerAdapters 일급 컬렉션화
5. RequestMapping 애노테이션 수정
6. Service 레이어 및 회원가입 검증로직 추가
