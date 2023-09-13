package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import samples.TestController;
import web.org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AnnotationScannerTest {

    @Test
    void 어노테이션이_붙은_클래스를_반환한다() {
        // given
        AnnotationScanner annotationScanner = new AnnotationScanner("samples");

        // when
        Map<Class<?>, Object> classWithInstance = annotationScanner.findAnnotatedClassWithInstance(Controller.class);

        // then
        assertThat(classWithInstance.keySet()).containsExactly(TestController.class);
    }

    @Test
    void 어노테이션이_붙지_않은_클래스는_반환하지_않는다() {
        // given
        AnnotationScanner annotationScanner = new AnnotationScanner("samples");

        // when
        Map<Class<?>, Object> classWithInstance = annotationScanner.findAnnotatedClassWithInstance(RequestMapping.class);

        // then
        assertThat(classWithInstance.keySet()).isEmpty();
    }
}
