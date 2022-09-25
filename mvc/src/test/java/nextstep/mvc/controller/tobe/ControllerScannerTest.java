package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import nextstep.web.annotation.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerScannerTest {

    @DisplayName("@Controller 가 붙은 클래스들을 불러온다.")
    @Test
    void scan() {
        final Map<Class<?>, Object> expected = new HashMap<>();
        expected.put(Test1Controller.class, new Test1Controller());
        expected.put(Test2Controller.class, new Test2Controller());

        final Map<Class<?>, Object> handlers = ControllerScanner.scan(getClass().getPackageName());

        for (Class<?> handlerClass : handlers.keySet()) {
            assertThat(handlers.get(handlerClass).getClass()).isSameAs(expected.get(handlerClass).getClass());
        }
    }

    @Controller
    static class Test1Controller {
        public Test1Controller() {
        }
    }

    @Controller
    static class Test2Controller {
        public Test2Controller() {
        }
    }
}
