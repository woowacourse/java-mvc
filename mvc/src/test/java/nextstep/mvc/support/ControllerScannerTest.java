package nextstep.mvc.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestAnnotationController;

class ControllerScannerTest {

    @Test
    @DisplayName("getControllers 메서드는 @Controller가 붙은 클래스를 모두 조회해서 map으로 반환한다.")
    void getControllers() {
        // when
        final Map<Class<?>, Object> actual = ControllerScanner.getControllers("samples");

        // then
        assertThat(actual).containsKey(TestAnnotationController.class);
        assertThat(actual.values())
                .hasOnlyElementsOfType(TestAnnotationController.class);
    }
}
