package nextstep.mvc.controller.scanner;

import static java.util.stream.Collectors.toSet;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import nextstep.mvc.controller.tobe.HandlerKey;
import nextstep.web.annotation.RequestMapping;

public class RequestMappingScanner {

    public static Set<Method> getRequestMappedMethod(Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .collect(toSet());
    }

    public static Set<HandlerKey> extractHandlerKeys(Method requestMappedMethod) {
        if(!requestMappedMethod.isAnnotationPresent(RequestMapping.class)) {
            throw new IllegalArgumentException("This method is not RequestMapped");
        }

        RequestMapping requestMapping = requestMappedMethod.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();

        return Arrays.stream(requestMapping.method())
            .map(method -> new HandlerKey(url, method))
            .collect(toSet());
    }
}
