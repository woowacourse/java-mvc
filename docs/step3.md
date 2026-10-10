# Step 3 - JSON View 구현하기

## 1. JsonView 구현

- [x] HTML 이외에 JSON으로 응답할 수 있다.
    - [x] `JsonView`를 구현한다.
    - [x] 응답 Content-Type을 `application/json;charset=UTF-8`로 설정한다.
    - [x] model에 데이터가 1개면 값을 그대로 JSON으로 변환한다.
    - [x] model에 데이터가 2개 이상이면 Map 형태 그대로 JSON으로 변환한다.

## 2. Legacy MVC 제거

- [x] app 모듈에 있는 모든 컨트롤러를 어노테이션 기반 MVC로 변경한다.
- [x] `asis` 패키지의 레거시 코드를 삭제해도 서비스가 정상 동작한다.
    - [x] `mvc.asis` 패키지를 제거한다.
    - [x] `ManualHandlerMapping`을 제거한다.
- [x] Legacy MVC를 제거한 뒤 `DispatcherServlet`을 mvc 패키지로 이동한다.
