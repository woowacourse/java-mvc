package reflection;

import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() {
        Reflections reflections = new Reflections("reflection.examples");

        Stream.of(Controller.class, Service.class, Repository.class)
                .map(reflections::getTypesAnnotatedWith)
                .flatMap(Set::stream)
                .distinct()
                .forEach(type -> log.info("발견한 클래스: {}", type.getName()));
    }
}
