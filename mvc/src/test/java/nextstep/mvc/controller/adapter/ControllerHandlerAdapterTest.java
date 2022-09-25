package nextstep.mvc.controller.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.mvc.controller.adapter.ControllerHandlerAdapter;
import nextstep.mvc.controller.adapter.HandlerAdapter;
import nextstep.mvc.controller.asis.ForwardController;
import org.junit.jupiter.api.Test;

class ControllerHandlerAdapterTest {

    @Test
    void support() {
        final HandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();
        final Object forwardController = new ForwardController("");

        final boolean actual = controllerHandlerAdapter.supports(forwardController);

        assertThat(actual).isTrue();
    }
}
