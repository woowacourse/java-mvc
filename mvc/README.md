# @MVC 프레임워크 구현하기

나만의 @MVC 프레임워크를 만들어보자.

이전 미션에서 HTTP 서버를 만들고 Controller 인터페이스를 활용해 MVC 프레임워크를 구현했다.

그런데 새로운 컨트롤러가 생길 때마다 RequestMapping 클래스에 URL과 컨트롤러 객체를 추가하는 게 번거롭다.
그리고 RequestMapping 클래스를 수정하면 MVC 프레임워크 영역까지 수정하게 된다.

비즈니스 로직 구현에만 집중 할 수 있도록 어노테이션 기반의 MVC 프레임워크로 개선해보자.
그리고 URL을 컨트롤러에 매핑하면서 HTTP 메서드(GET, POST, PUT, DELETE 등)도 매핑 조건에 포함시키자.

@RequestMapping()에 method 설정이 되어 있지 않으면 모든 HTTP method를 지원해야 한다.
HTTP 메서드와 URL를 매핑 조건으로 만들어보자.

아래와 같은 컨트롤러를 지원하는 프레임워크를 구현한다.

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

## 기능 요구 사항

프레임워크 영역과 서비스 영역을 분리하기 위해 멀티모듈을 적용했다.

mvc 모듈은 프레임워크, app 모듈은 프로덕션 영역이다.

### @MVC Framework 테스트 통과하기

효과적인 실습을 위해 새로운 MVC 프레임워크의 뼈대가 되는 코드(mvc 모듈의 webmvc.servlet.mvc.tobe 패키지)와 테스트 코드를 제공하고 있다.
Tomcat 구현하기 미션에서 적용한 Controller 인터페이스는 2단계 미션에서 통합할 예정이다.

- [x] AnnotationHandlerMappingTest 클래스의 테스트 성공
    - [x] Controller 인터페이스는 그대로 두고 미션을 진행한다.

- [x] @RequestMapping()에 method 설정이 되어 있지 않으면 모든 HTTP method를 지원해야 한다.

### JspView 클래스를 구현한다.

`webmvc.org.springframework.web.servlet.view` 패키지에서 JspView 클래스를 찾을 수 있다.

- [ ] DispatcherServlet 클래스의 service 메서드에서 어떤 부분이 뷰에 대한 처리를 하고 있는지 파악해서 JspView 클래스로 옮겨보자.
