package nextstep.mvc.handler.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.assembler.ComponentScanner;
import nextstep.mvc.handler.asis.Controller;
import nextstep.mvc.support.annotation.ControllerAnnotationUtils;
import nextstep.mvc.support.annotation.RequestMappingAnnotationUtils;
import nextstep.web.support.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private final String[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(String... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        Arrays.stream(basePackages)
                .flatMap(path -> findControllers(path).stream())
                .forEach(controller -> registerController(controller));
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        return handlerExecutions.get(new HandlerKey(requestPath, requestMethod));
    }

    private Set<Class<?>> findControllers(String path) {
        return ControllerAnnotationUtils.findControllers(path);
    }

    public void registerController(Class<?> controller) {
        List<Method> methods = RequestMappingAnnotationUtils.findByController(controller);
        methods.stream()
                .flatMap(key -> RequestMappingAnnotationUtils.getHandlerKeys(key).stream())
                .forEach(key -> handlerExecutions.put(key, HandlerExecution.of(controller)));
    }

    public void registerController(Controller controller) {
        List<Method> methods = RequestMappingAnnotationUtils.findByController(controller.getClass());
        methods.stream()
                .flatMap(key -> RequestMappingAnnotationUtils.getHandlerKeys(key).stream())
                .forEach(key -> handlerExecutions.put(key, HandlerExecution.of(controller)));
    }
}
