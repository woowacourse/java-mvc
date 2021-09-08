package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        //given
        Reflections reflections = new Reflections("examples", new TypeAnnotationsScanner(), new SubTypesScanner());
        //when
        final Set<Class<?>> serviceAnnotated = reflections.getTypesAnnotatedWith(Service.class);
        final Set<Class<?>> repositoryAnnotated = reflections.getTypesAnnotatedWith(Repository.class);
        final Set<Class<?>> controllerAnnotated = reflections.getTypesAnnotatedWith(Controller.class);
        //then
        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        serviceAnnotated.forEach(it -> log.info("Service Annotated: {}", it.getSimpleName()));
        repositoryAnnotated.forEach(it -> log.info("Repository Annotated: {}", it.getSimpleName()));
        controllerAnnotated.forEach(it -> log.info("Controller Annotated: {}", it.getSimpleName()));
    }
}
