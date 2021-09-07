# MVC 프레임워크 구현하기

## todo
- [x] `ReflectionTest`의 테스트를 통과시킨다
- [x] `ReflectionsTest`를 완성하여 로그를 출력한다
- [x] `Junit3TestRunner`를 이용하여 `Junit3Test`에서 test로 시작하는 메서드를 실행한다
- [x] `Junit4TestRunner`를 이용하여 `Junit4Test`에서 `@MyTest`어노테이션이 있는 메서드를 실행한다

## 기능 요구 사항

- [ ] jacoco 설정 추가

- [ ] JspView render 메서드 구현

- [ ] 기존의 컨트롤러를 annotation 기반으로 변경하더라도 정상 작동
- [ ] 새로운 TestController를 포함하면 해당 컨트롤러의 기능이 애플리케이션에서 작동

##### TestController 소스코드

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
