# @MVC 구현하기 - step2

## 이전 구현

[톰캣 구현하기🐱](https://github.com/BETTERFUTURE4/jwp-dashboard-http)

## 기능 요구 사항

## 미션 설명

### 2단계 - 점진적인 리팩터링

새로운 MVC 프레임워크를 추가하면 기존에 구현한 컨트롤러 인터페이스 기반 MVC 프레임워크로 만든 컨트롤러도 변경 해야 할까? 실습 코드는 컨트롤러 클래스의 갯수가 적고 시스템 영향도 파악이 어렵지 않고 금방 바꿀
수 있다. 하지만 실제 서비스 되는 프로덕션 코드는 복잡하고 영향 범위가 훨씬 크다. 수백 개에서 수천 개의 클래스를 변경해야 될 수도 있다. 변경이 쉽지 않기 때문에 기존 코드를 유지하면서 신규 기능을 추가해야
한다.

### Legacy MVC와 @MVC 통합하기

컨트롤러 인터페이스 기반 MVC 프레임워크와 @MVC 프레임워크가 공존하도록 만들자. 예를 들면, 회원가입 컨트롤러를 아래처럼 어노테이션 기반 컨트롤러로 변경해도 정상 동작해야 한다.

```java

@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest req, HttpServletResponse res) {
        //...
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req, HttpServletResponse res) {
        //...
    }
}
```

## 클래스 다이어그램

![diagram](https://techcourse-storage.s3.ap-northeast-2.amazonaws.com/8f08fc94e3714965b0918e9f42c61ba6)

## 체크리스트

- [ ] ControllerScanner 클래스에서 @Controller가 붙은 클래스를 찾을 수 있다.
- [ ] HandlerMappingRegistry 클래스에서 HandlerMapping을 처리하도록 구현했다.
- [ ] HandlerAdapterRegistry 클래스에서 HandlerAdapter를 처리하도록 구현했다.

## 피드백

### step2 피드백
