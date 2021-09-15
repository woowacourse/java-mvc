# MVC 프레임워크 구현하기

<br/>

## Annotation Based MVC Framework

- 컨트롤러 인터페이스 기반 MVC 프레임워크 -> 어노테이션 기반 MVC 프레임워크 개선
    - [x] URL, HTTP 메소드를 매핑 조건에 포함
```java
@Controller
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "/get-test", method = RequestMethod.GET)
    public ModelAndView findUserId(HttpServletRequest request, HttpServletResponse response) {
        log.info("test controller get method");
        final ModelAndView modelAndView = new ModelAndView(new JspView("/get-test.jsp"));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }

    @RequestMapping(value = "/post-test", method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
        log.info("test controller post method");
        final ModelAndView modelAndView = new ModelAndView(new JspView("/post-test.jsp"));
        modelAndView.addObject("id", request.getAttribute("id"));
        return modelAndView;
    }
}
```
- 멀티모듈 분리
    - mvc 모듈은 프레임워크 영역, app 모듈은 서비스 영역
- [x] `AnnotationHandlerMappingTest`를 참고해서 실패하는 테스트가 통과하게 구현
- JspView
    - [x] `DispatcherServlet`를 참고해서 View 처리를 어떻게 하는지 확인하고, todo 완료

<br/>

## Legacy MVC와 Annotation Based MVC 통합

- [x] 컨트롤러 인터페이스 기반 MVC 프레임워크 + 어노테이션 기반 MVC 프레임워크 공존
  - e.g. `RegisterController`를 컨트롤러 인터페이스 기반 컨트롤러 -> 어노테이션 기반 컨트롤러 변경해도 정상 동작
```java
@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest req, HttpServletResponse res) {
        //
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req, HttpServletResponse res) {
        //
    }
}
```

<br/>

## 리팩터링
### AnnotationHandlerMapping
- [x] `HandlerScanner` 추가 for 역할/책임 분리
  - @Controller 어노테이션이 붙은 클래스를 찾고, 해당 클래스의 인스턴스 생성

### DispatcherServlet
- [x] `HandlerAdapter` 추가 for 다른 Handler 타입
  - Request URI에 따라 ManualHandlerMapping -> Controller, AnnoationHandlerMapping -> HandlerExecution 반환  

<br/>

## View 구현하기
- [ ] JSP를 지원하는 JspView 구현
- [ ] REST API를 지원하는 JsonView 구현
  - e.g. `UserController`를 추가했을 때, 정상 동작
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

- [ ] JSON <-> Java Object 변환할 때, Jackson 라이브러리 사용 -> 공식 문서 참고
- [ ] JSON으로 응답할 때, Content-Type은 `MediaType.APPLICATION_JSON_UTF8_VALUE`로 반환
- [ ] Model에 데이터가 1개면 값을 그대로 반환, 2개 이상이면 값을 JSON으로 변환해서 반환

<br/>

## Legacy MVC 제거하기
- [ ] 모든 컨트롤러 -> 어노테이션 기반 컨트롤러 변경
- [ ] `asis` 패키지에 있는 레거시 코드를 삭제해도 정상 동작
