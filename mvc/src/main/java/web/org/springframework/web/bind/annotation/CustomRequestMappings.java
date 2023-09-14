package web.org.springframework.web.bind.annotation;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public enum CustomRequestMappings {

    GET(GetMapping.class),
    POST(PostMapping.class),
    PUT(PutMapping.class),
    DELETE(DeleteMapping.class),
    PATCH(PatchMapping.class);

    private final Class<?> annotation;

    CustomRequestMappings(Class<?> annotation) {
        this.annotation = annotation;
    }

    public static boolean isAnyMatch(Annotation annotation) {
        return Arrays.stream(values())
                .anyMatch(each -> each.annotation.equals(annotation.annotationType()));
    }
}
