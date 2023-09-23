package web.org.springframework.web.bind.annotation;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public enum MethodMapping {
    GET(GetMapping.class),
    POST(PostMapping.class),
    PUT(PutMapping.class),
    PATCH(PatchMapping.class),
    HEAD(HeadMapping.class),
    DELETE(DeleteMapping.class);

    private final Class<?> annotation;

    MethodMapping(final Class<?> annotation) {
        this.annotation = annotation;
    }

    public static boolean isAnyMatchAnnotation(final Annotation[] annotations) {
        return Arrays.stream(values())
                .anyMatch(methodMapping -> isAnyMatch(annotations, methodMapping));
    }

    private static boolean isAnyMatch(final Annotation[] annotations, final MethodMapping methodMapping) {
        return Arrays.stream(annotations)
                .anyMatch(each -> each.annotationType().equals(methodMapping.annotation));
    }
}
