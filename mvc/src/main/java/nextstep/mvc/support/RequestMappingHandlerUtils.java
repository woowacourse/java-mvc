package nextstep.mvc.support;

import static nextstep.mvc.support.AnnotationHandlerUtils.getMethodsAnnotatedWith;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.mapper.tobe.HandlerKey;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

public class RequestMappingHandlerUtils {

    private RequestMappingHandlerUtils() {
    }

    public static List<Method> findByController(Class<?> controller) {
        return getMethodsAnnotatedWith(controller, RequestMapping.class);
    }

    public static Set<HandlerKey> getHandlerKeys(Method method) {
        String path = method.getAnnotation(RequestMapping.class).value();
        RequestMethod[] requestMethods = method.getAnnotation(RequestMapping.class).method();

        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(path, requestMethod))
                .collect(Collectors.toSet());
    }

    public static boolean isMapped(Method method, String requestPath, RequestMethod requestMethod) {
        String mappedPath = method.getAnnotation(RequestMapping.class).value();
        List<RequestMethod> mappedMethods = Arrays.asList(method.getAnnotation(RequestMapping.class).method());

        return mappedPath.equals(requestPath) && mappedMethods.contains(requestMethod);
    }
}
