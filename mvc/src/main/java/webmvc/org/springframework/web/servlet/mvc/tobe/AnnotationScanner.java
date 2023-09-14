package webmvc.org.springframework.web.servlet.mvc.tobe;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AnnotationScanner {

    private static final Logger log = LoggerFactory.getLogger(AnnotationScanner.class);

    private final Reflections reflections;

    public AnnotationScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getAnnotatedClasses(Class<? extends Annotation> annotation) {
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(annotation);
        return classes.stream()
                .collect(Collectors.toMap(Function.identity(), this::generateInstance));
    }

    private Object generateInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            log.error("NoArgConstructor doesn't exist.");
        } catch (SecurityException e) {
            log.error("Check permission");
        } catch (IllegalAccessException e) {
            log.error("Constructor is not accessible.");
        } catch (IllegalArgumentException e) {
            log.error("Type of Arguments doesn't matched.");
        } catch (InstantiationException e) {
            log.error("The instance is abstract class.");
        } catch (InvocationTargetException e) {
            log.error("Exception occurs during constructing.");
        } catch (ExceptionInInitializerError error) {
            log.error("Initializing fails.");
        }
        throw new IllegalArgumentException("Getting instance using constructor fails.");
    }
}
