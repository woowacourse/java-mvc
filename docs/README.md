# 1단계 @MVC 프레임워크 구현하기

## 구현 목록

* [x] basePackage가 주어졌을 때 @Controller가 붙은 클래스 불러오기
* [x] 메서드에서 @RequestMapping이 붙은 메서드 가져오기
* [x] HTTP 메서드와 URL을 매핑 조건으로 설정하기
* [x] jspView 책임 재설정
* [x] @RequestMapping()에 method 설정이 되어 있지 않으면 모든 HTTP method를 지원하기

## 서블릿 학습 테스트

* 특정 path로 요청을 n번 보냈을 때, 시작할 때 init() 호출, (doFilter(), service() n번 호출), destroy() 호출
* init()과 destroy()는 한번만 호출되고 doFilter()와 service()는 여러번 호출 된다. 
* 서블릿의 인스턴스 변수는 다른 스레드와 공유되기 때문에 유의해서 사용하기

* contentType 설정은 write 하기 전에 하기
