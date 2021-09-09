# MVC 프레임워크 구현하기

## todo
- [x] `ReflectionTest`의 테스트를 통과시킨다
- [x] `ReflectionsTest`를 완성하여 로그를 출력한다
- [x] `Junit3TestRunner`를 이용하여 `Junit3Test`에서 test로 시작하는 메서드를 실행한다
- [x] `Junit4TestRunner`를 이용하여 `Junit4Test`에서 `@MyTest`어노테이션이 있는 메서드를 실행한다

## 기능 요구 사항

- [x] jacoco 설정 추가 - 이미 추가되어 있다 (근데 다른 사람들 PR 보면 작동 안 함)

- [x] AnnotationHandlerMapping 구현
- [ ] AnnotationHandlerMapping 리팩토링
  - [ ] RequestMapping이 클래스 단위로 있는경우
  - [ ] 클래스, 메서드 둘 다 있는 경우

- [ ] 기존의 컨트롤러를 annotation 기반으로 변경하더라도 정상 작동
  - [x] DispatcherServlet이 ModelAndView를 처리할 수 있도록 기능 추가
  - [x] JspView render 메서드 구현
- [ ] 새로운 TestController를 포함하면 해당 컨트롤러의 기능이 애플리케이션에서 작동

- [ ] TODO 처리

### 생각
* Reflections를 이용하여 주어진 basePackage 안에서 @Controller 어노테이션이 있는 클래스를 가지고 온다 (@RestController도 있으면 좋겠지만 일단 패스)
  * 하나의 메서드에 여러 RequestMethod가 들어갈 수 있다. 그래서 여러 RequestMethod가 있다면 각각이 하나의 Handler가 되도록 했는데 이게 맞으려나?
  * HandlerExecution이 뭐지 - 완료
  * HandlerKey의 정적 팩토리 메서드를 만들었다. 주 생성자를 닫아야 할까? 주 생성자를 사용할 상황도 있는데? - 기존에는 닫았지만, 이번엔 닫지 않음
  * mvc 패키지 안에 asis, tobe 패키지가 있는데, 이게 도대체 뭘까
    * as-is와 to-be, 즉 "기존의 것", 그리고 "개선 방향" 인건가? 그럼 지워도 되나?
* @RequestMapping 어노테이션을 찾아야 한다. 타깃이 Type, Method 둘 다 되기 때문에 클래스 단위, 메서드 단위로 있는지 확인해야 한다

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
