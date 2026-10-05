package reflection;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

//하나의 Class를 들여다보는 Reflection”에서 “패키지 전체를 뒤져 원하는 Class들을 찾는 것”으로 확장
class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples");

        Set<Class<?>> controllers =
                reflections.getTypesAnnotatedWith(Controller.class);

        Set<Class<?>> services =
                reflections.getTypesAnnotatedWith(Service.class);

        Set<Class<?>> repositories =
                reflections.getTypesAnnotatedWith(Repository.class);

        controllers.forEach(clazz -> log.info("{}", clazz.getName()));
        services.forEach(clazz -> log.info("{}", clazz.getName()));
        repositories.forEach(clazz -> log.info("{}", clazz.getName()));
    }
}
