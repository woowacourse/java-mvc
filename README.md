# @MVC 구현하기

### 1단계 미션

#### (HandlerKey) 핸들러 매핑을 위한 HandlerKey를 구현한다.
  - [ ] Request Uri 정보를 갖는다.
  - [ ] Request Http Method 정보를 갖는다.

#### (HandlerExecution) 메서드 정보를 가지고 있는 HandlerExecution을 구현한다.
  - [ ] HandlerExecution의 필드로 Method를 선언한다.
  - [ ] HandlerExecution에 Method가 선언되어 있는 클래스 정보를 가져와서 메서드를 호출할 수 있도록 한다.

#### (AnnotationHandlerMapping) Controller, RequestMapping 어노테이션이 선언된 클래스와 메서드 정보를 가져와 핸들러 매핑 정보로 저장한다.
  - [ ] Controller 어노테이션이 선언되어 있는 클래스를 가져온다.
  - [ ] RequestMapping 어노테이션이 선언되어 있는 메서드를 가져온다.
    - [ ] RequestMapping이 가지고 있는 uri, method를 정보를 가져와 HandlerKey를 생성한다.
    - [ ] RequestMapping 어노테이션이 선언된 메서드 정보를 가져와 HandlerExecution을 생성한다.
    - [ ] HandlerKey와 HandlerExecution을 key-value로 매핑한다.
    - [ ] 클라이언트 요청 정보를 가지고 있는 HttpServletRequest를 이용해서 HandlerKey를 생성한 후 매핑된 HandlerExecution을 찾아 반환한다.
