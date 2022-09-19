# @MVC 구현하기
> 나만의 @MVC 프레임워크를 만들어보자.

## 💻 기능 요구 사항

### 1단계 @MVC 프레임워크 구현하기

> 기존의 MVC 프레임워크를 어노테이션 기반으로 변경한다.  
> - 개발자가 비지니스 로직 구현에만 집중할 수 있게 된다!

#### AnnotationHandlerMapping
- [X] `initialize()`
  - [X] `@Controller`가 있는 클래스를 컨트롤러로 인식하고 모두 찾아온다.
  - [X] 각 메서드의 `@RequestMapping` 속성을 확인해 HandlerKey와 HanderExecution을 생성 및 등록한다.
    - [X] `value`(URL), `method`(HTTP 메서드) 속성을 가진다. 
- [X] `getHandler(request)`
  - [X] request를 확인해 HandlerKey를 만든다.
  - [X] HandlerKey로 HanderExecution을 찾고 리턴한다.

  
