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
- [ ] `HandlerScanner` 추가 for 역할/책임 분리
  - @Controller 어노테이션이 붙은 클래스를 찾고, 해당 클래스의 인스턴스 생성

### DispatcherServlet
- [ ] `HandlerAdapter` 추가 for 다른 Handler 타입
  - Request URI에 따라 ManualHandlerMapping -> Controller, AnnoationHandlerMapping -> HandlerExecution 반환  
