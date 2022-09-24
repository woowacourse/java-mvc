# @MVC 구현하기

---

## 1단계 구현 내용 정리

- [x] AnnotationHandlerMappingTest가 정상 동작한다.
  - [x] 어노테이션 기반으로 HTTP 메서드와 URL에 따라 컨트롤러를 매핑해줄 수 있다.
- [x] DispatcherServlet에서 HandlerMapping 인터페이스를 활용하여 AnnotationHandlerMapping과 ManualHandlerMapping 둘다 처리할 수 있다.
  - [x] DispatcherServlet에서 instanceof 를 이용하여 두 가지 HandlerMapping 을 처리해준다.
- [x] JspView 를 이용해서 redirect 혹은 forward 를 해줄 수 있다.

## 2단계 기능 요구사항 정리

### Legacy MVC 와 @MVC 통합하기

컨트롤러 인터페이스 기반 MVC 프레임워크와 @MVC 프레임워크가 공존하도록 만들자.
예를 들면, 회원가입 컨트롤러를 아래처럼 어노테이션 기반 컨트롤러로 변경해도 정상 동작해야 한다.

```java
@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest req, HttpServletResponse res) {
        ...
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req, HttpServletResponse res) {
        ...
    }
}
```

## 2단계 구현 내용 정리

- [x] ControllerScanner 클래스에서 @Controller가 붙은 클래스를 찾을 수 있다.
- [ ] HandlerMappingRegistry 클래스에서 HandlerMapping을 처리하도록 구현했다.
- [ ] HandlerAdapterRegistry 클래스에서 HandlerAdapter를 처리하도록 구현했다.
