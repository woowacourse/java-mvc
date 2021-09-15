# MVC 프레임워크 구현하기 🚀

## step1, 2
- [x] learning-test 수행하기
- [x] AnnotationHandlerMapping 구현하기
- [x] JSPView 구현하기
- [x] LegacyCode도 동작하게 하기
- [x] HandlerAdapter 이용하기
- [x] ControllerScanner로 메서드 분리하기

## step3
- [ ] JsonView, JSPView 구현하기
- [ ] JSON으로 응답할 때 ContentType은 MediaType.APPLICATION_JSON_UTF8_VALUE으로 반환
- [ ] model에 데이터가 1개면 값을 그대로 반환하고 2개 이상이면 Map 형태 그대로 JSON으로 변환해서 반환
- [ ] app 모듈에 있는 모든 컨트롤러를 어노테이션 기반 MVC로 변경
- [ ] asis 패키지에 있는 레거시 코드를 삭제해도 서비스가 정상 동작하도록 구현
- [ ] 아래 컨트롤러를 추가했을 때 정상 동작하도록 구현
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
