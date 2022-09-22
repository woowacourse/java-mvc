package nextstep.mvc.controller.asis;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class ControllerHandlerAdapterTest {

    @Test
    @DisplayName("Controller의 구현체라면 true를 반환한다.")
    void supportsWhenTrue() {
        ControllerHandlerAdapter handlerAdapter = new ControllerHandlerAdapter();
        Controller controller = new ControllerForTest();
        assertThat(handlerAdapter.supports(controller)).isTrue();
    }

    @Test
    @DisplayName("Controller의 구현체가 아니라면 false를 반환한다.")
    void supportsWhenFalse() {
        ControllerHandlerAdapter handlerAdapter = new ControllerHandlerAdapter();
        Object object = new Object();
        assertThat(handlerAdapter.supports(object)).isFalse();
    }

    private class ControllerForTest implements Controller {
        @Override
        public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
            return null;
        }
    }

}
