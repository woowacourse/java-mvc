package nextstep.util;

import static java.util.stream.Collectors.toSet;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionUtils {

    private static final Logger log = LoggerFactory.getLogger(ReflectionUtils.class);

    public static Set<Class<?>> scanClassByAnnotationWith(List<String> basePackagePaths,
                                                          Class<? extends Annotation> annotationClass) {
        log.info("어노테이션이 붙은 클래스를 스캔할 패키지 : {}", basePackagePaths);

        return basePackagePaths.stream()
            .map(Reflections::new)
            .flatMap(reflections -> reflections.
                getTypesAnnotatedWith(annotationClass).
                stream()
            )
            .collect(toSet());
    }

    public static Set<Method> scanAllMethodByAnnotationWith(Class<?> objType,
                                                            Class<? extends Annotation> annotationClass) {
        Method[] declaredMethods = objType.getDeclaredMethods();
        return Arrays.stream(declaredMethods)
            .filter(declaredMethod -> declaredMethod.isAnnotationPresent(annotationClass))
            .peek(declaredMethod -> log
                .info("어노테이션 명 : {}, 메소드 명 : {}", annotationClass.getSimpleName(),
                    declaredMethod.getName()))
            .collect(toSet());
    }
}
