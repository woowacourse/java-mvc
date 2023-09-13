package web.org.springframework.web.bind.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public enum RequestMappings {

    GET(GetMapping.class),
    POST(PostMapping.class),
    PUT(PutMapping.class),
    DELETE(DeleteMapping.class),
    PATCH(PatchMapping.class);

    private final Class<?> clazz;

    RequestMappings(Class<?> clazz) {
        this.clazz = clazz;
    }

    public static boolean contains(Class<?> clazz) {
        return Arrays.stream(values())
                .anyMatch(requestMapping -> requestMapping.clazz.equals(clazz));
    }

    public static boolean hasAnyOfRequestMappings(Method method) {
        Annotation[] annotations = method.getDeclaredAnnotations();

        return Arrays.stream(annotations)
                .anyMatch(RequestMappings::hasRequestMapping);
    }

    private static boolean hasRequestMapping(Annotation annotation) {
       return Arrays.stream(values())
                .anyMatch(requestMapping -> requestMapping.clazz.equals(annotation.annotationType()));
    }
}
