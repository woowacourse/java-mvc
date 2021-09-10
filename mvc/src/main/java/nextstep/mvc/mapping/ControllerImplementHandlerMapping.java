package nextstep.mvc.mapping;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.core.ApplicationContext;
import nextstep.mvc.WebMvcConfigurer;
import nextstep.mvc.controller.ControllerContainer;

public class ControllerImplementHandlerMapping implements HandlerMapping {

    private final ApplicationContext applicationContext;
    private final ControllerContainer controllerContainer;

    public ControllerImplementHandlerMapping(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.controllerContainer = new ControllerContainer();
    }

    @Override
    public void initialize() {
        final WebMvcConfigurer webMvcConfigurer = applicationContext.getBean(WebMvcConfigurer.class);
        if(webMvcConfigurer != null) {
            webMvcConfigurer.addController(controllerContainer);
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return controllerContainer.getController(request.getRequestURI());
    }
}
