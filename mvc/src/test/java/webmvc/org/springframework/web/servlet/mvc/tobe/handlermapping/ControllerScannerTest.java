package webmvc.org.springframework.web.servlet.mvc.tobe.handlermapping;

import context.org.springframework.stereotype.Controller;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    private static final String BASE_PACKAGE = "samples";

    private ControllerScanner controllerScanner = new ControllerScanner();

    @Test
    void scan() {
        //given
        Set<Class<?>> controllers = controllerScanner.scan(BASE_PACKAGE);

        //when, then
        assertThat(controllers.size()).isEqualTo(1);
    }

    @Test
    void createControllerInstance() {
        //given
        Reflections reflections = new Reflections(BASE_PACKAGE);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        Map<Class<?>, Object> controllerInstance = controllerScanner.createControllerInstance(controllers);

        assertThat(controllerInstance.size()).isEqualTo(1);
    }
}
