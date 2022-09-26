package nextstep.mvc.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class ControllerScannerTest {

    @Test
    void getControllers() {
        ControllerScanner controllerScanner = new ControllerScanner("samples");
        Set<Class<?>> controllers = controllerScanner.getControllers();
        List<String> names = controllers.stream()
                .map(Class::getSimpleName)
                .collect(Collectors.toList());

        assertThat(names).contains("TestController");
    }
}
