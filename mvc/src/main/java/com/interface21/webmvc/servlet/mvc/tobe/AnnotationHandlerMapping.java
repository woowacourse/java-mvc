package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.Set;
import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HandlerExecutions();
    }

    /*
    controllers.put("/", new ForwardController("/index.jsp"));
            controllers.put("/login", new LoginController());
            controllers.put("/login/view", new LoginViewController());
            controllers.put("/logout", new LogoutController());
            controllers.put("/register/view", new RegisterViewController());
            controllers.put("/register", new RegisterController());
    * */
    public void initialize() throws Exception {
        log.info("Initialized AnnotationHandlerMapping!");

        Set<Class<?>> controllers = findControllers(basePackage);
        handlerExecutions.registerController(controllers);
    }

    private Set<Class<?>> findControllers(Object[] basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    public Object getHandler(HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);
        return handlerExecutions.getHandlerExecution(handlerKey);
    }
}
