package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    //  implementation 'org.reflections:reflections:0.10.2' 이곳에 있는 걸로 보임
    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples", Scanners.TypesAnnotated);

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.

        List<Class<? extends Annotation>> target = List.of(Controller.class, Service.class, Repository.class);
        target.forEach(it -> {
            Set<Class<?>> results = reflections.getTypesAnnotatedWith(it);
            results.forEach(result -> log.info("annotation : {} / result = {}", it.getSimpleName(), result.getSimpleName()));
        });
    }
}
