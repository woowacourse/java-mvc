# @MVC 구현하기

## 기본 흐름

### main에서 직접 Tomcat 객체 생성 및 구동

- Tomcat에 Connector 설정
- Tomcat에 Webapp 설정
- Tomcat 구동 및 Server 대기

```java
public class Application {

    private static final int DEFAULT_PORT = 8080;

    public static void main(final String[] args) throws Exception {
        final var tomcat = new Tomcat();
        tomcat.setConnector(createConnector(DEFAULT_PORT)); // port=8080, bindOnInit=false
        final var docBase = new File("app/src/main/webapp/").getAbsolutePath();
        tomcat.addWebapp("", docBase);

        tomcat.start();
        tomcat.getServer().await();
    }
    // ...
}
```

### WebApplicationInitializer 인터페이스의 구현체 자동 생성 및 구동

- 재구현한 `onStartup` 메서드 자동 호출됨
- 기본적으로 매개변수로 넘겨받은 ServletContext에 Servlet들 추가되도록 직접 설정
- DispatcherServlet 객체 & HandlerMapping 객체도 생성만 하여 의존성에 맞게 설정

```java
public class AppWebApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(final ServletContext servletContext) {
        final var dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new ManualHandlerMapping());

        final var dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
```

### 설정된 DispatcherServlet & HandlerMapping 초기화

- DispatcherServlet 객체 생성 초기에는 빈 리스트를 handlerMappings로 관리
- `addHandlerMapping` 메서드를 호출하여 초기 상태의 HandlerMapping 객체 받음
- servletContext에서 설정된 서블릿들의 `init` 메서드 호출
- DispatcherServlet의 `init` 메서드 호출시, 관리 중인 각 HandlerMapping의 `initialize` 호출

```java
public class DispatcherServlet extends HttpServlet {

    private final List<HandlerMapping> handlerMappings;

    public DispatcherServlet() {
        this.handlerMappings = new ArrayList<>();
    }

    @Override
    public void init() {
        log.info("Initialize DispatcherServlet!");
        handlerMappings.forEach(HandlerMapping::initialize);
    }
    
    // ...
}
```
