package web.org.springframework.web.bind.annotation;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public enum HttpMappings {
    GET(GetMapping.class),
    POST(PostMapping.class),
    PUT(PutMapping.class),
    PATCH(PatchMapping.class),
    DELETE(DeleteMapping.class),
    DEFAULT(RequestMapping.class);

    private final Class<?> annotationType;

    HttpMappings(final Class<?> annotationType) {
        this.annotationType = annotationType;
    }

    public static boolean isAnyMatch(final Annotation annotation) {
        return Arrays.stream(values())
                .anyMatch(value -> value.annotationType.equals(annotation.annotationType()));
    }
}
