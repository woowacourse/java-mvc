<p align="center">
    <img src="./woowacourse.png" alt="우아한테크코스" width="250px">
</p>

# @MVC 구현하기

---

![Generic badge](https://img.shields.io/badge/Level4-mvc-green.svg)
![Generic badge](https://img.shields.io/badge/test-57_passed-blue.svg)
![Generic badge](https://img.shields.io/badge/version-2.0.0-brightgreen.svg)

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

## Step3 체크리스트

- [ ] 힌트에서 제공한 UserController 컨트롤러가 json 형태로 응답을 반환한다.
- [x] 레거시 코드를 삭제하고 서버를 띄워도 정상 동작한다.

### 기능 요구사항

- [x] JspView 클래스를 구현한다.
- [ ] JsonView 클래스를 구현한다.
- [x] Legacy MVC 제거하기
    - [x] app 모듈에 있는 모든 컨트롤러를 어노테이션 기반 MVC로 변경한다.
    - [x] asis 패키지에 있는 레거시 코드를 삭제해도 서비스가 정상 동작하도록 리팩터링하자.

<br><br>

## Step2 체크리스트

---

- [x] ControllerScanner 클래스에서 @Controller가 붙은 클래스를 찾을 수 있다.
- [x] HandlerMappingRegistry 클래스에서 HandlerMapping을 처리하도록 구현했다.
- [x] HandlerAdapterRegistry 클래스에서 HandlerAdapter를 처리하도록 구현했다.

<br><br>

## 1차 피드백 체크리스트

- [x] Map.entrySet().forEach()를 Map.forEach로 개선
    - StreamAPI를 사용하지 않고 단순 순회, log 작업만 수행하기에 entrySet()으로 스트림화 할 필요가 없었음
- [x] ControllerScanner의 중복 filtering 관련
    - 컨트롤러 필터링과 메서드 필터링으로 목적이 다름, 재차 필터링 없을 시 equals,hashCode 등도 전달됨

<br><br>

## Step1 체크리스트

---

- [x] AnnotationHandlerMappingTest가 정상 동작한다.
- [x] ~~DispatcherServlet에서 Controller와 HandlerMapping 인터페이스를 둘다 처리할 수 있다.~~
- [x] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여<br>AnnotationHandlerMapping과 ManualHandlerMapping 둘다 처리할 수 있다.

<br><br>

## 1차 피드백 체크리스트

- [x] RegisterController#execute 계속 같은 정보로 사용자가 생성되고 있음
- [x] RequestMappingHandlerAdapter#handle 인라인 처리
- [x] ControllerMappingHandlerAdapter 네이밍 개선

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
