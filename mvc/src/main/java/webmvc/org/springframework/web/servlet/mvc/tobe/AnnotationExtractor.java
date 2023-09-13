package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationExtractor {

    public static Object extractByMethodName(Annotation annotation, String methodName) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        try {
            Method method = annotationType.getDeclaredMethod(methodName);
            return method.invoke(annotation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RequestMappingPathNotProvidedException();
    }
}
