# @MVC 구현하기

---

## 1단계 구현 내용 정리

- [x] AnnotationHandlerMappingTest가 정상 동작한다.
  - [x] 어노테이션 기반으로 HTTP 메서드와 URL에 따라 컨트롤러를 매핑해줄 수 있다.
- [x] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여 AnnotationHandlerMapping과 ManualHandlerMapping 둘다 처리할 수 있다.
  - [x] DispatcherServlet에서 instanceof 를 이용하여 두 가지 HandlerMapping 을 처리해준다.
- [x] JspView 를 이용해서 redirect 혹은 forward 를 해줄 수 있다.

## 2단계 기능 요구사항 정리

### Legacy MVC 와 @MVC 통합하기

컨트롤러 인터페이스 기반 MVC 프레임워크와 @MVC 프레임워크가 공존하도록 만들자.
예를 들면, 회원가입 컨트롤러를 아래처럼 어노테이션 기반 컨트롤러로 변경해도 정상 동작해야 한다.

```java
@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest req, HttpServletResponse res) {
        ...
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req, HttpServletResponse res) {
        ...
    }
}
```

## 2단계 구현 내용 정리

- [x] ControllerScanner 클래스에서 @Controller가 붙은 클래스를 찾을 수 있다.
  - [x] 기존에 DispaterServlet에 있던 해당 로직을 ControllerScanner로 위임한다.
  - [x] ControllerScanner는 Controller 어노테이션이 붙은 클래스를 찾고, 해당 인스턴스를 생성해 반환한다.
- [x] Adapter 를 구현하여 Adapter의 지원(support) 여부에 따라 요청을 처리한다.
  - [x] DispaterServlet에서는 Adapter를 이용하여 처리하도록 수정한다. 
  - [x] 기존에 DispaterServlet에 있던 renderView() 메소드를 ModelAndView 쪽으로 이동시킨다. 
- [x] HandlerMappingRegistry 클래스에서 HandlerMapping을 처리하도록 구현했다.
  - [x] HandlerMappingRegistry 에 HandlerMapping 인스턴스를 추가할 수 있다.
  - [x] HandlerMappingRegistry 를 통해서 핸들러를 찾을 수 있다.
- [x] HandlerAdapterRegistry 클래스에서 HandlerAdapter를 처리하도록 구현했다.
  - [x] HandlerAdapterRegistry 에 HandlerAdapter 를 추가할 수 있다.
  - [x] HandlerAdapterRegistry 를 통해서 요청을 처리 가능한 핸들러를 찾을 수 있다.

## 3단계 기능 요구사항 정리

현재는 DispatcherServlet에서 JSP를 직접 처리하고 있다.
화면에 대한 책임은 View 클래스가 갖도록 만들자.
DispatcherServlet이 아닌 뷰에서 JSP를 반환하도록 수정하자.
그리고 REST API를 지원할 수 있도록 Json 뷰를 추가하자.

## 3단계 구현 내용 정리

- [x] JspView 클래스를 구현한다.
  - DispatcherServlet 클래스의 service 메서드에서 어떤 부분이 뷰에 대한 처리를 하고 있는지 파악해서 JspView 클래스로 옮겨보자.
- [x] JsonView 클래스를 구현한다.
  - HTTP Request Body로 JSON 타입의 데이터를 받았을 때 어떻게 자바에서 처리할지 고민해보고 JsonView 클래스를 구현해보자.
  - [x] model에 데이터가 1개면 값을 그대로 반환하고 2개 이상이면 Map 형태 그대로 JSON 으로 변환해서 반환한다.
- [x] Legacy MVC 제거하기
  - [x] app 모듈에 있는 모든 컨트롤러를 어노테이션 기반 MVC로 변경한다.
  - [x] asis 패키지에 있는 레거시 코드를 삭제해도 서비스가 정상 동작하도록 리팩터링하자.
- [x] 힌트에서 제공한 UserController 컨트롤러가 json 형태로 응답을 반환한다.
- [x] 레거시 코드를 삭제하고 서버를 띄워도 정상 동작한다.

```java
@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        final String account = request.getParameter("account");
        log.debug("user id : {}", account);

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        final User user = InMemoryUserRepository.findByAccount(account)
                .orElseThrow();

        modelAndView.addObject("user", user);
        return modelAndView;
    }
}
```

