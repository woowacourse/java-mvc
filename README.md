# MVC 프레임워크 구현하기 🚀

## step1, 2
- [x] learning-test 수행하기
- [x] AnnotationHandlerMapping 구현하기
- [x] JSPView 구현하기
- [x] LegacyCode도 동작하게 하기
- [x] HandlerAdapter 이용하기
- [x] ControllerScanner로 메서드 분리하기

## step3
- [x] JsonView, JSPView 구현하기
- [x] JSON으로 응답할 때 ContentType은 MediaType.APPLICATION_JSON_UTF8_VALUE으로 반환
- [x] model에 데이터가 1개면 값을 그대로 반환하고 2개 이상이면 Map 형태 그대로 JSON으로 변환해서 반환
- [x] app 모듈에 있는 모든 컨트롤러를 어노테이션 기반 MVC로 변경
- [x] asis 패키지에 있는 레거시 코드를 삭제해도 서비스가 정상 동작하도록 구현
- [x] 아래 컨트롤러를 추가했을 때 정상 동작하도록 구현
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

### 소롱 리뷰 반영
- [ ] 가입 시 account가 중복되지 않도록 검증 추가
- [ ] AnnotationHandlerMapping Methods명을 명시적으로 변경하기

### 추가
- [x] DTO로 service에 넘기도록 한번 감싸기
