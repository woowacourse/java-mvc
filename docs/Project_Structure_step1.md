### AnnotationHandlerMapping 구조

- 주어진 basePackage 내부에 있는 모든 컨트롤러 탐색 (@Controller가 적용된 클래스 탐색)
- 각 컨트롤러마다의 API 메서드를 탐색 (@RequestMapping이 적용된 메서드 탐색)
- API 메서드 단위로 HandlerExecution를 만들어 등록

### HandlerExecution 구조

- HandlerExecution는 자체적으로 Controller 인스턴스를 필드로 보유
- handle() 메서드 호출시 인자인 request 정보를 기반으로 Controller의 적절한 메서드를 찾아 실행

### 부연 설명

- AnnotationHandlerMapping에 HandlerExecution를 등록할때는 API 메서드 단위로 하는데,
  각 HandlerExecution은 API 메서드가 아닌 컨트롤러 인스턴스를 필드로 가지고 있은 이유
    - HandlerExecution가 API 메서드를 필드로 가지게 할 경우 해당 메서드를 실행하기 위해 HandlerExecution마다 각각 따로
      컨트롤러 인스턴스를 생성해야 합니다! (Method.invoke(컨트롤러_인스턴스, 인자1, 인자2, ...))
    - 만약 컨트롤러에 API 메서드가 5개 있다면 5개의 HandlerExecution가 생성되어야 하고, 생성된 HandlerExecution는 내부적으로
      동일한 종류의 컨트롤러 인스턴스를 각각 따로 생성해야합니다!
    - 이 부분이 부자연스럽다고 판단했고, 결국 지금 구조처럼 하나의 컨트롤러 인스턴스를 생성하고 여러 HandlerExecution가 이를 참조하는 구조로 설계를
      진행했습니다!
