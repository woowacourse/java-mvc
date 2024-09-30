# 1단계 @MVC 프레임워크 구현하기

## 구현 목록

### 1단계
* [x] basePackage가 주어졌을 때 @Controller가 붙은 클래스 불러오기
* [x] 메서드에서 @RequestMapping이 붙은 메서드 가져오기
* [x] HTTP 메서드와 URL을 매핑 조건으로 설정하기
* [x] jspView 책임 재설정
* [x] @RequestMapping()에 method 설정이 되어 있지 않으면 모든 HTTP method를 지원하기

### 2단계
* [x] Legacy MVC와 @MVC 통합하기 위한 적절한 추상화

### 3단계
* [ ] JsonView 클래스 구현
* [x] JSON으로 응답할 때 ContentType은 MediaType.APPLICATION_JSON_UTF8_VALUE으로 반환해야 한다.
* [x] model에 데이터가 1개면 값을 그대로 반환하고 2개 이상이면 Map 형태 그대로 JSON으로 변환해서 반환한다.

## 서블릿 학습 테스트

* 특정 path로 요청을 n번 보냈을 때, 시작할 때 init() 호출, (doFilter(), service() n번 호출), destroy() 호출
* init()과 destroy()는 한번만 호출되고 doFilter()와 service()는 여러번 호출 된다. 
* 서블릿의 인스턴스 변수는 다른 스레드와 공유되기 때문에 유의해서 사용하기

* contentType 설정은 write 하기 전에 하기
