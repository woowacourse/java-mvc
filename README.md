<p align="center">
    <img src="./woowacourse.png" alt="우아한테크코스" width="250px">
</p>

# @MVC 구현하기

---

![Generic badge](https://img.shields.io/badge/Level4-mvc-green.svg)
![Generic badge](https://img.shields.io/badge/test-2_passed-blue.svg)
![Generic badge](https://img.shields.io/badge/version-1.0.0-brightgreen.svg)

> 우아한테크코스 웹 백엔드 4기, @MVC 구현하기 저장소입니다.

<img src="./diagram.png" alt="java-lotto-operation" width="768px">

<br><br>

## 미션 설명

---

> 나만의 @MVC 프레임워크를 만들어보자.

- 이전 미션에서 HTTP 서버를 만들고 Controller 인터페이스를 활용해 MVC 프레임워크를 구현했다.
- 그런데 새로운 컨트롤러가 생길 때마다 RequestMapping 클래스에 URL과 컨트롤러 객체를 추가하는 게 번거롭다.
- 그리고 RequestMapping 클래스를 수정하면 MVC 프레임워크 영역까지 수정하게 된다.
  비즈니스 로직 구현에만 집중 할 수 있도록 어노테이션 기반의 MVC 프레임워크로 개선해보자.
- 그리고 URL을 컨트롤러에 매핑하면서 HTTP 메서드(GET, POST, PUT, DELETE 등)도 매핑 조건에 포함시키자.
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

<br><br>

## 기능 요구 사항

---

@MVC Freamwork
어노테이션 기반의 MVC 프레임워크를 구현한다.

<br><br>

## 체크리스트

---

- [x] AnnotationHandlerMappingTest가 정상 동작한다.
- [x] ~~DispatcherServlet에서 Controller와 HandlerMapping 인터페이스를 둘다 처리할 수 있다.~~
- [x] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여<br>AnnotationHandlerMapping과 ManualHandlerMapping 둘다 처리할 수 있다.

<br><br>

## 1차 피드백 체크리스트

- [ ] RegisterController#execute 계속 같은 정보로 사용자가 생성되고 있음
- [ ] RequestMappingHandlerAdapter#handle 인라인 처리 필요
- [ ] ControllerMappingHandlerAdapter 네이밍 개선

<br><br>

## 참고사항

---

- 프레임워크 영역과 서비스 영역을 분리하기 위해 멀티모듈을 적용했다.
- mvc 모듈은 프레임워크, app 모듈은 서비스 영역이다.

<br><br>

## 힌트

- AnnotationHandlerMappingTest 클래스의 실패하는 테스트를 통과하도록 구현해보자.
- JspView는 어떻게 구현 할 수 있을까? DispatcherServlet에서 뷰 처리를 어떻게 하는지 참고해서 todo를 채워보자.

<br><br>
