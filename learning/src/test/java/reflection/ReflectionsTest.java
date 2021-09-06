package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("examples");

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        final Set<Class<?>> classesWithControllerAnnotation = reflections.getTypesAnnotatedWith(Controller.class);

        final Set<Class<?>> classesWithServiceAnnotation = reflections.getTypesAnnotatedWith(Service.class);

        final Set<Class<?>> classesWithRepositoryAnnotation = reflections.getTypesAnnotatedWith(Repository.class);

        final Set<Class<?>> allClassesWithAnnotation = Stream.of(classesWithControllerAnnotation, classesWithServiceAnnotation, classesWithRepositoryAnnotation)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        for (final Class<?> clazz : allClassesWithAnnotation) {
            log.info("class name: " + clazz.getSimpleName());
        }
    }
}
