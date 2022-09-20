# @MVC 구현하기

## 이전 구현

[톰캣 구현하기🐱](https://github.com/BETTERFUTURE4/jwp-dashboard-http)

## 기능 요구 사항

## 미션 설명

- 나만의 @MVC 프레임워크를 만들어보자. 이전 미션에서 HTTP 서버를 만들고 Controller 인터페이스를 활용해 MVC 프레임워크를 구현했다. 그런데 새로운 컨트롤러가 생길 때마다 RequestMapping
클래스에 URL과 컨트롤러 객체를 추가하는 게 번거롭다. 그리고 RequestMapping 클래스를 수정하면 MVC 프레임워크 영역까지 수정하게 된다. 비즈니스 로직 구현에만 집중 할 수 있도록 어노테이션 기반의
MVC 프레임워크로 개선해보자.

- 그리고 URL을 컨트롤러에 매핑하면서 HTTP 메서드(GET, POST, PUT, DELETE 등)도 매핑 조건에 포함시키자. HTTP 메서드와 URL를 매핑 조건으로 만들어보자.

- 아래와 같은 컨트롤러를 지원하는 프레임워크를 구현한다.

### @MVC Freamwork

- 어노테이션 기반의 MVC 프레임워크를 구현한다.

### 클래스 다이어그램

![diagram](https://techcourse-storage.s3.ap-northeast-2.amazonaws.com/77ca3ea3fe7e47c2801ce58636c9d3f0)

## 체크리스트

- [x] AnnotationHandlerMappingTest 가 정상 동작한다.
- [x] DispatcherServlet 에서 HandlerMapping 인터페이스를 활용하여 AnnotationHandlerMapping 과 ManualHandlerMapping 둘다 처리할 수 있다.
