package reflection;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reflection.annotation.Controller;
import reflection.annotation.Repository;
import reflection.annotation.Service;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("reflection.examples", new SubTypesScanner(false));

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        List<? extends Class<?>> collect = reflections.getSubTypesOf(Object.class).stream()
                .collect(Collectors.toList());
        for (Class<?> aClass : collect) {
            for (Annotation annotation : aClass.getAnnotations()) {
                if (annotation instanceof Controller || annotation instanceof Service || annotation instanceof Repository) {
                    log.info("class: {}, annotation: {}", aClass.getName(), annotation.getClass());
                }
            }
        }
    }
}
