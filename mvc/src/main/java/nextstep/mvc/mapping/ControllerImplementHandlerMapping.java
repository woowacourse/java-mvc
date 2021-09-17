package nextstep.mvc.mapping;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.core.ApplicationContext;
import nextstep.core.exception.BeanException;
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
        try {
            final WebMvcConfigurer webMvcConfigurer = applicationContext.getBean(WebMvcConfigurer.class);
            webMvcConfigurer.addController(controllerContainer);
        } catch (BeanException e) {
            return;
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return controllerContainer.getController(request.getRequestURI());
    }
}
