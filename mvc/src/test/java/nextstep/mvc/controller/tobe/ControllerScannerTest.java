package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    private final ControllerScanner controllerScanner = new ControllerScanner("samples");

    @Test
    @DisplayName("해당 패키지의 컨트롤러들을 가져올 수 있다.")
    void getControllers() {
        final Set<Class<?>> controllers = controllerScanner.getControllers();

        assertThat(controllers).contains(TestController.class);
    }

    @Test
    @DisplayName("method가 주어질 때 컨트롤러로 인스턴스화할 수 있다.")
    void instantiate() throws NoSuchMethodException {
        final Method method = TestController.class.getDeclaredMethod(
                "findUserId", HttpServletRequest.class, HttpServletResponse.class
        );

        final Object actual = controllerScanner.instantiate(method);

        assertThat(actual).isInstanceOf(TestController.class);
    }
}