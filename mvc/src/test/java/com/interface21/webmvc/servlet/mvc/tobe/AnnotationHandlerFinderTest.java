package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.web.bind.annotation.RequestMapping;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class AnnotationHandlerFinderTest {

    @DisplayName("어노테이션으로 핸들러를 찾는다.")
    @Test
    void findHandlersTest() {
        // given
        String[] basePackage = {"samples"};
        AnnotationHandlerFinder annotationHandlerFinder = new AnnotationHandlerFinder(basePackage);

        // when
        Class<? extends Annotation> targetAnnotation = RequestMapping.class;
        List<Handler> handlers = annotationHandlerFinder.findHandlers(targetAnnotation);

        // then
        Method[] expected = TestController.class.getDeclaredMethods();
        assertThat(handlers).hasSize(expected.length)
            .extracting("method")
            .containsExactlyInAnyOrderElementsOf(Arrays.asList(expected));
    }

    @DisplayName("같은 클래스의 핸들러들은 동일한 인스턴스를 참조한다.")
    @Test
    void findHandlersTest1() {
        // given
        String[] basePackage = {"samples"};
        AnnotationHandlerFinder annotationHandlerFinder = new AnnotationHandlerFinder(basePackage);

        // when
        Class<? extends Annotation> targetAnnotation = RequestMapping.class;
        List<Handler> handlers = annotationHandlerFinder.findHandlers(targetAnnotation);

        // then
        assertThat(handlers)
            .extracting("instance")
            .satisfies(instances -> assertThat(instances.stream().distinct().count()).isEqualTo(1));
    }
}
