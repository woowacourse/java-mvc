package reflection;

import java.util.List;
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

        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        log.info("@Controller classes = {}", getClassNames(controllerClasses));

        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        log.info("@Service classes = {}", getClassNames(serviceClasses));

        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);
        log.info("@Repository classes = {}", getClassNames(repositoryClasses));
    }

    private List<String> getClassNames(Set<Class<?>> classes) {
        return classes.stream()
                .map(Class::getSimpleName)
                .toList();
    }
}
