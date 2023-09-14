package webmvc.org.springframework.web.servlet.mvc.tobe;

import web.org.springframework.web.bind.annotation.RequestMethod;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class RequestMappingExtractor {

    public static String extractRequestURI(Annotation annotation) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        try {
            Method method = annotationType.getDeclaredMethod("value");
            return (String) method.invoke(annotation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RequestMappingPathNotProvidedException();
    }

    public static RequestMethod[] extractRequestMethod(Annotation annotation) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        try {
            Method method = annotationType.getDeclaredMethod("method");
            return (RequestMethod[]) method.invoke(annotation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RequestMappingPathNotProvidedException();
    }
}
