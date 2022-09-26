# Spring @MVC 구현하기

## 서버 구동 과정

### 1단계) `app` 프로젝트에서 `main` 실행

- Tomcat 인스턴스 생성
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

        tomcat.start(); // 톰캣 구동
        tomcat.getServer().await(); // 서버 대기 (blocking 상태로 평새 유지)
    }
    // ...
}
```

### 2단계) `tomcat.start()` 실행시 `mvc` 프로젝트의 ServletContainerInitializer 구현체 구동

- `mvc` 프로젝트의 resources에 지정된 ServletContainerInitializer 구현체 경로를 확인
    - `mvc.src.main.resources.META-INF.services.jakarta.servlet.ServletContainerInitializer`

```
nextstep.web.NextstepServletContainerInitializer
```

- 경로에 존재하는 `ServletContainerInitializer` 구현체 생성 및 `onStartup` 메서드 실행
- `onStartup` 메서드는 매개변수로 `WebApplicationInitializer` 구현체들을 스캔한 클래스를 매개변수로 전달 받음.
- `NextstepServletContainerInitializer`에서는 이 `WebApplicationInitializer` 구현체들을 인스턴스화하여 `onStartup` 메서드 실행
    - 만일 `app` 프로젝트에 `WebApplicationInitializer` 구현체가 하나도
      없다면 `No Spring WebApplicationInitializer types detected on classpath` 출력

```java
package nextstep.web;

@HandlesTypes(WebApplicationInitializer.class)
public class NextstepServletContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext servletContext)
            throws ServletException {
        final List<WebApplicationInitializer> initializers = new LinkedList<>();

        if (webAppInitializerClasses != null) {
            for (Class<?> waiClass : webAppInitializerClasses) {
                try {
                    initializers.add((WebApplicationInitializer) waiClass.getDeclaredConstructor().newInstance());
                } catch (Throwable e) {
                    throw new ServletException("Failed to instantiate WebApplicationInitializer class", e);
                }
            }
        }

        if (initializers.isEmpty()) {
            servletContext.log("No Spring WebApplicationInitializer types detected on classpath");
            return;
        }

        for (WebApplicationInitializer initializer : initializers) {
            initializer.onStartup(servletContext);
        }
    }
}
```

### 3단계) `app` 프로젝트의 AppWebApplicationInitializer의 `onStartup` 실행

- 기본적으로 매개변수로 넘겨받은 ServletContext에 직접 생성한 Servlet들을 추가
- DispatcherServlet 객체에 필요한 의존성들도 주입
    - HandlerMapping 객체, HandlerAdapter 객체들 전부 생성하여 주입
- 다만, 서블릿의 `init`은 직접적으로 호출하지 않음. (`onStartup` 종료 후에 실행)

```java
public class AppWebApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(final ServletContext servletContext) {
        log.info("Start AppWebApplication Initializer");
        final var dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse"));
        dispatcherServlet.addHandlerAdapter(new ModelAndViewHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new ViewNameHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new VoidHandlerAdapter());

        final var dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
}
```

### 4단계) Filter 스캔하여 인스턴스 생성 후 등록 및 초기화

- `@WebFilter` 어노테이션이 붙은 `Filter` 구현체들을 찾아 컨테이너에 등록
  - 만일 `@WebFilter`이 붙은 클래스가 `Filter` 인터페이스의 구현체가 아닌 경우 예외 발생
  - 만일 `Filter` 인터페이스의 구현체더라도 `@WebFilter`이 붙어있지 않으면 Reflection의 스캔 대상에서 누락됨!

- 서블릿 초기화 전에 각 `Filter`들의 `init` 메서드 호출하여 순차적으로 초기화

### 5단계) 설정된 DispatcherServlet 초기화

- DispatcherServlet 객체 생성 초기에는 비어있는 HandlerMappingRegistry 객체와 HandlerAdapterRegistry 객체 관리
    - `addHandlerMapping` 메서드는 초기 상태의 HandlerMapping 객체들 받아서 HandlerMappingRegistry에 등록
    - `addHandlerAdapter` 메서드는 호출하여 초기 상태의 HandlerAdapter 객체들 받아서 HandlerAdapterRegistry에 등록

- `WebApplicationInitializer`의 `onStartup` 실행 종료 후 servletContext에서 설정된 서블릿들의 `init` 메서드를 호출

- DispatcherServlet의 `init` 메서드 호출시, 주입받은 의존성들의 초기화 메서드(e.g., `initialize`) 호출.

```java
public class DispatcherServlet extends HttpServlet {

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        handlerMappingRegistry.init();
    }
    // ...
}
```
