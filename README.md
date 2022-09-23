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


## 2단계 - 점진적인 리팩터링

### 체크 리스트

- [ ] ControllerScanner 클래스에서 `@Controller`가 붙은 클래스를 찾을 수 있다.
- [ ] HandlerMappingRegistry 클래스에서 HandlerMapping을 처리하도록 구현했다.
- [ ] HandlerAdapterRegistry 클래스에서 HandlerAdapter를 처리하도록 구현했다.

### 작업 목록

- [ ] `ControllerScanner` 객체 생성 및 `@Controller`가 붙은 클래스 찾는 책임 부여
- [ ] `HandlerAdaptor` 구현
  - 반환값이 다른 핸들러들에 대해서 동일한 인터페이스로 처리가능하도록 변경한다.
  - 힌트를 보지않고 구현한 뒤 힌트를 보고 개선한다.
