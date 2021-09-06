package nextstep.mvc.mapper.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.support.ControllerMappingHandlerUtils;
import nextstep.mvc.support.RequestMappingHandlerUtils;
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
                .forEach(this::registerController);
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        return handlerExecutions.get(new HandlerKey(requestPath, requestMethod));
    }

    private Set<Class<?>> findControllers(String path) {
        return ControllerMappingHandlerUtils.findControllers(path);
    }

    private void registerController(Class<?> controller) {
        List<Method> methods = RequestMappingHandlerUtils.findByController(controller);
        methods.stream()
                .flatMap(key -> RequestMappingHandlerUtils.getHandlerKeys(key).stream())
                .forEach(key -> handlerExecutions.put(key, HandlerExecution.of(controller)));
    }
}
