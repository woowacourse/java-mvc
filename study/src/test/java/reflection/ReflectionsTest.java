package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

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
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        final Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        final Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);

        assertThat(controllers.size()).isEqualTo(1);
        assertThat(services.size()).isEqualTo(1);
        assertThat(repositories.size()).isEqualTo(2);

        controllers.forEach(controller -> log.info("controller: " + controller.getName()));
        System.out.println();

        services.forEach(service -> log.info("service: " + service.getName()));
        System.out.println();

        repositories.forEach(repository -> log.info("repo: " + repository.getName()));
        System.out.println();
    }
}
