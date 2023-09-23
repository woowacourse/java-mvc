package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import samples.TestController;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AnnotationScannerTest {

    private final String basePackage = "samples";
    private final AnnotationScanner annotationScanner = new AnnotationScanner(basePackage);

    @Test
    void 패키지_내부의_컨트롤러_애너테이션이_적용된_클래스의_인스턴스와_prefix를_전부_반환한다() {
        // when
        final Map<Class<?>, ControllerInstance> result = annotationScanner.scanControllers();

        // then
        final ControllerInstance controllerInstance = result.get(TestController.class);
        assertSoftly(softly -> {
            assertThat(controllerInstance.getInstance().getClass()).isEqualTo(TestController.class);
            assertThat(controllerInstance.getUriPrefix()).isEqualTo("/test");
        });
    }

    @Test
    void 입력받은_타입에_해당하는_RequestMapping이_적용된_모든_메서드를_반환한다() {
        // given
        final Set<Class<?>> types = Set.of(TestController.class);

        // when
        final Set<Method> result = annotationScanner.scanHttpMappingMethods(types);

        // then
        assertThat(result).hasSize(6);
    }
}
