# @MVC 구현하기

## Level 1 - @MVC 프레임워크 구현하기
- [x] AnnotationHandlerMapping 
  - [x] selectedPackage 미사용
  - [x] 불필요 공백 제거 
- [x] JspView
  - [x] 내부 상수에 대한 불필요 클래스 사용 제거 
- [ ] AnnotationHandlerAdapter
  - [ ] handle 메서드 테스트
- [x] RequestMapping에 HttpMethod가 배열로 들어갈 경우 구현
- [x] when() mockito 메서드 
  - [x] when이 아닌 given

## Level 2 - 점진적인 리팩터링
- [x] ManualHandlerMapping, 추가된 핸들러 중 사용하지 않는 클래스는 삭제
- [x] 공백 제거 
- [x] Registry 클래스 패키지 수정
- [x] equals, hashcode 메서드 수정 이유 설명
  - 리팩터링 미션하면서 ResgisterController를 어노테이션을 사용하는 방식으로 수정함.
  - 그 과정에서 입력한 Request에 따른 Handler가 검색되지 않는 에러가 발생함.
  - 해당 과정에서 문제점이 equals 메서드인지 의심이 되어 수정해보았음.

## Level 3 - JsonView 구현
- [x] 리팩터링 이후 실패하는 테스트 코드 개선
- [x] JsonView 힌트 적용
