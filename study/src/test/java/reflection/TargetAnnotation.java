package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;

public class TargetAnnotation {
    private final String annotationName;

    public TargetAnnotation(String annotationName) {
        this.annotationName = annotationName;
    }

    public boolean included(Method method) {
        return Arrays.stream(method.getAnnotations())
                .anyMatch(annotation -> annotation.annotationType().getSimpleName().equals(annotationName));
    }
}
