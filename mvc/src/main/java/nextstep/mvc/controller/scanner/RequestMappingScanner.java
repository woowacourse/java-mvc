package nextstep.mvc.controller.scanner;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.web.annotation.RequestMapping;

public class RequestMappingScanner {

    private static RequestMappingScanner INSTANCE = new RequestMappingScanner();

    private RequestMappingScanner() {
    }

    public static RequestMappingScanner getInstance() {
        return INSTANCE;
    }

    public Set<?> getAllAnnotations(Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .collect(Collectors.toSet());
    }
}
