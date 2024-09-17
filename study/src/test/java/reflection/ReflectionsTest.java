package reflection;

import java.lang.annotation.Annotation;

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
        // main/java/reflection/exmaples 경로의 모든 클래스를 가져오는 듯
        Reflections reflections = new Reflections("reflection.examples");

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
        Class<? extends Annotation>[] annotations = new Class[]{Controller.class, Service.class, Repository.class};

        for (Class<? extends Annotation> annotation : annotations) {
            // 이렇게 3개의 annotation에 대해 for문을 돌면서 로그를 찍는게 최선인 것 같다.
            reflections.getTypesAnnotatedWith(annotation).forEach(found -> log.info(found.getSimpleName()));
        }
    }
}
