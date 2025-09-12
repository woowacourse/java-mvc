package reflection;

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

        // 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        Set<Class<?>> controllerClazzs = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> serviceClazzs = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositoryClazzs = reflections.getTypesAnnotatedWith(Repository.class);

        StringBuilder sb = new StringBuilder();
        sb.append("Controller").append("\n");
        sb.append(controllerClazzs.toString()).append("\n\n");

        sb.append("Service").append("\n");
        sb.append(serviceClazzs.toString()).append("\n\n");

        sb.append("Repository").append("\n");
        sb.append(repositoryClazzs.toString()).append("\n\n");

        System.out.println(sb);
    }
}
