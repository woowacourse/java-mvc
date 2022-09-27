package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @Test
    @DisplayName("특정 디렉터리의 컨트롤러 클래스와 인스턴스를 스캔한다.")
    void getControllers() {
        Object[] basePackages = new Object[1];
        basePackages[0] = "samples";
        ControllerScanner controllerScanner = new ControllerScanner(basePackages);

        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        Set<Entry<Class<?>, Object>> entries = controllers.entrySet();

        assertThat(controllers.size()).isEqualTo(1);
        for (Entry<Class<?>, Object> entry : entries) {
            assertThat(entry.getKey()).isEqualTo(TestController.class);
            assertThat(entry.getValue()).isInstanceOf(entry.getKey());
        }
    }
}
