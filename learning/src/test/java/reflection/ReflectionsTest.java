package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.SubmissionPublisher;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);
    private static final List<Class<?>> TARGET_ANNOTATION = Arrays.asList(
        new Class[]{Controller.class, Service.class, Repository.class});

    @Test
    void showAnnotationClass() throws Exception {
        //given
        Reflections reflections = new Reflections("examples", new SubTypesScanner(false));
        final Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);
        //when
        int answer = 0;
        for (Class<?> singleClass : allClasses) {
            System.out.println(singleClass.toString());
            answer += countAnnotation(singleClass);
        }

        //then
        assertThat(answer).isEqualTo(4);
    }

    private int countAnnotation(Class<?> singleClass) {
        final Annotation[] annotationNames = singleClass.getAnnotations();
        int answer = 0;
        for (Annotation annotation : annotationNames) {
            answer = checkAnnotation(singleClass, answer, annotation);

        }
        return answer;
    }

    private int checkAnnotation(Class<?> singleClass, int answer, Annotation annotation) {
        if (TARGET_ANNOTATION.contains(annotation.annotationType())) {
            log.info("해당 %s은 어노테이션을 가지고 있습니다.", singleClass.toString());
            answer++;
        }
        return answer;
    }
}
