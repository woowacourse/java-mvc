# @MVC 구현하기

### 1단계 미션

#### (HandlerKey) 핸들러 매핑을 위한 HandlerKey를 구현한다.
  - [x] Request Uri 정보를 갖는다.
  - [x] Request Http Method 정보를 갖는다.

#### (HandlerExecution) 메서드 정보를 가지고 있는 HandlerExecution을 구현한다.
  - [x] HandlerExecution의 필드로 Object(Controller)를 선언한다.
  - [x] HandlerExecution의 필드로 Method를 선언한다.
  - [x] HandlerExecution의 handle 메서드를 통해 결과값(ModelAndView)를 반환하도록 기능을 구현한다. 

#### (AnnotationHandlerMapping) Controller, RequestMapping 어노테이션이 선언된 클래스와 메서드 정보를 가져와 핸들러 매핑 정보로 저장한다.
  - [x] Controller 어노테이션이 선언되어 있는 클래스를 가져온다.
  - [x] RequestMapping 어노테이션이 선언되어 있는 메서드를 가져온다.
    - [x] RequestMapping이 가지고 있는 uri, method를 정보를 가져와 HandlerKey를 생성한다.
    - [x] RequestMapping 어노테이션이 선언된 메서드 정보를 가져와 HandlerExecution을 생성한다.
    - [x] HandlerKey와 HandlerExecution을 key-value로 매핑한다.
    - [x] 클라이언트 요청 정보를 가지고 있는 HttpServletRequest를 이용해서 HandlerKey를 생성한 후 매핑된 HandlerExecution을 찾아 반환한다.
  - [x] HandlerKey에 매핑되는 HandlerExecution이 존재하지 않을 경우 null을 반환한다.
