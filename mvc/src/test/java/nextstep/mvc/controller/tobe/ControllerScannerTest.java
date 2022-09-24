package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestAnnotationController;

class ControllerScannerTest {

    @DisplayName("컨트롤러 어노테이션이 붙은 컨트롤러를 찾는다.")
    @Test
    void getAnnotationController() {
        final ControllerScanner scanner = new ControllerScanner(TestAnnotationController.class.getPackageName());
        final Set<Class<?>> annotationController = scanner.getAnnotationController();

        assertThat(annotationController.contains(TestAnnotationController.class)).isTrue();
    }
}
