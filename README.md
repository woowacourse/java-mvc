# 만들면서 배우는 스프링

## @MVC 구현하기

### 학습목표
- @MVC를 구현하면서 MVC 구조와 MVC의 각 역할을 이해한다.
- 새로운 기술을 점진적으로 적용하는 방법을 학습한다.

### 시작 가이드
1. 미션을 시작하기 전에 학습 테스트를 먼저 진행합니다.
    - [x] [Junit3TestRunner](study/src/test/java/reflection/Junit3TestRunner.java)
    - [x] [Junit4TestRunner](study/src/test/java/reflection/Junit4TestRunner.java)
    - [x] [ReflectionTest](study/src/test/java/reflection/ReflectionTest.java)
    - [x] [ReflectionsTest](study/src/test/java/reflection/ReflectionsTest.java)
    - 나머지 학습 테스트는 강의 시간에 풀어봅시다.
2. 학습 테스트를 완료하면 LMS의 1단계 미션부터 진행합니다.

## 학습 테스트
1. [Reflection API](study/src/test/java/reflection)
2. [Servlet](study/src/test/java/servlet)

## 기능 구현 목록
- [x] 핸들러 매핑 구현
- [x] jsp view 구현

## 리팩토링 목록
- [x] reflectionTest 생성자 테스트 수정
- [x] handlerExecutions 초기화
  - [x] basepackage sample 중복 삭제
  - [x] mapping 범위 컨트롤러로 축소
- [x] reflection test runner 스트림을 for문으로 변경
- [x] @RequestMapping()에 method 설정이 되어 있지 않으면 모든 HTTP method 지원
