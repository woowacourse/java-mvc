package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import com.google.common.graph.PredecessorsFunction;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {

        Stream.of(
            findClassesByAnnotation(Controller.class).stream(),
            findClassesByAnnotation(Service.class).stream(),
            findClassesByAnnotation(Repository.class).stream()
        )
            .flatMap(Function.identity())
            .forEach(clazz -> log.info(clazz.getName()));
    }

    private Set<Class<?>> findClassesByAnnotation(Class<? extends Annotation> annotation) {
        Reflections reflections = new Reflections("examples");

        return reflections.getTypesAnnotatedWith(annotation);
    }
}
