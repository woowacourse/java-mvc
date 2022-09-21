package nextstep.mvc.controller.scanner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.controller.tobe.HandlerKey;
import nextstep.web.annotation.RequestMapping;

public class RequestMappingScanner {

    private static RequestMappingScanner INSTANCE = new RequestMappingScanner();

    private RequestMappingScanner() {
    }

    public static RequestMappingScanner getInstance() {
        return INSTANCE;
    }

    public Set<Method> getAllAnnotations(Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .collect(Collectors.toSet());
    }

    public Set<HandlerKey> extractHandlerKeys(Method requestMappedMethod) {
        if (!requestMappedMethod.isAnnotationPresent(RequestMapping.class)) {
            throw new IllegalArgumentException("Method is not RequestMapped");
        }

        RequestMapping requestMapping = requestMappedMethod.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();

        return Arrays.stream(requestMapping.method())
            .map(method -> new HandlerKey(url, method))
            .collect(Collectors.toSet());
    }
}
