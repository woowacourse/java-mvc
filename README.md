# @MVC 구현하기

## 1단계 - `@MVC 프레임워크` 구현하기

### 클래스 다이어그램

![image](https://user-images.githubusercontent.com/52696169/190914907-beb1d419-d550-4b4e-9f4d-b3948fbb551b.png)

### 체크 리스트

- [x] AnnotationHandlerMappingTest가 정상 동작한다.
- [x] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여 AnnotationHandlerMapping과 ManualHandlerMapping 둘다 처리할 수 있다.

### 작업 목록

- [x] AnnotationHandlerMappingTest를 성공시킨다.
- [x] AnnotationHandlerMapping도 돌아가도록 수정한다.
  - [x] AnnotationHandlerMapping을 이용하여 `login/view` 페이지를 띄운다.
  - [x] AnnotationHandlerMapping을 이용하여 모든 페이지가 띄워지도록 한다. ( 인덱스 페이지 제외 )
