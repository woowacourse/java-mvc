package nextstep.mvc.controller.scanner;

import static java.util.stream.Collectors.toSet;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import nextstep.web.annotation.RequestMapping;

public class RequestMappingScanner {

    public static Set<Method> getRequestMappedMethod(Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .collect(toSet());
    }
}
