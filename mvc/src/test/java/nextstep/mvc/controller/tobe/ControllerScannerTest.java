package nextstep.mvc.controller.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {
    @Test
    @DisplayName("Controller 어노테이션이 붙은 클래스들을 찾아 인스턴스를 생성한다.")
    void createInstances() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // given, when
        ControllerScanner controllerScanner = new ControllerScanner("samples");
        List<Object> instances = controllerScanner.createInstances();

        // when
        assertThat(instances.stream().anyMatch(instance -> instance instanceof TestController))
                .isTrue();
    }
}
