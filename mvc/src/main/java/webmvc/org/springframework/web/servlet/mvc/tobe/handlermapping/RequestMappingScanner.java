package webmvc.org.springframework.web.servlet.mvc.tobe.handlermapping;

import web.org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RequestMappingScanner {

    public List<Method> scanRequestMappingMethods(Set<Class<?>> controllers) {
        return controllers.stream()
                .map(Class::getDeclaredMethods).flatMap(Stream::of)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }
}
