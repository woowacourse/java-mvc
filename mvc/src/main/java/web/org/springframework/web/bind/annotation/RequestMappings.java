package web.org.springframework.web.bind.annotation;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public enum RequestMappings {

    GET(GetMapping.class),
    POST(PostMapping.class),
    PUT(PutMapping.class),
    DELETE(DeleteMapping.class),
    PATCH(PatchMapping.class),
    BASIC(RequestMapping.class);

    private final Class<?> annotation;

    RequestMappings(Class<?> annotation) {
        this.annotation = annotation;
    }

    public static boolean isAnyMatch(Annotation annotation) {
        Class<? extends Annotation> annotationType = annotation.annotationType();

        return Arrays.stream(values())
                .anyMatch(each -> each.annotation.equals(annotationType));
    }
}
