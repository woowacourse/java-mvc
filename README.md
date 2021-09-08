# MVC 프레임워크 구현하기

<br/>

## Annotation Based MVC Framework

- [ ] 컨트롤러 인터페이스 기반 MVC 프레임워크 -> 어노테이션 기반 MVC 프레임워크 개선
    - [ ] URL, HTTP 메소드를 매핑 조건에 포함
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
- `AnnotationHandlerMappingTest.java`를 참고해서 실패하는 테스트가 통과하게 구현
- JspView
    - `DispatcherServlet.java`를 참고해서 View 처리를 어떻게 하는지 확인하고, todo 완료
