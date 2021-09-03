package nextstep.mvc.controller.scanner;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerScannerTest {

    @DisplayName("컨트롤러를 찾는다.")
    @Test
    void getAllControllers() {
        Set<Class<?>> allControllers = ControllerScanner
            .getAllControllers("nextstep.mvc.controller.scanner.test_classes");

        List<String> classNames = allControllers.stream()
            .map(Class::getSimpleName)
            .collect(toList());

        assertThat(classNames).containsOnly("Test1");
    }
}
