package nextstep.mvc.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerAdapterTest {
    private final ControllerAdapter controllerAdapter = new ControllerAdapter();

    @DisplayName("Handler가 Controller의 구현체이면 지원한다.")
    @Test
    void supports_true() {
        final boolean actual = controllerAdapter.supports(mock(Controller.class));
        assertThat(actual).isTrue();
    }

    @DisplayName("Handler가 Controller의 구현체가 아니면 지원하지 않는다.")
    @Test
    void supports_false() {
        final boolean actual = controllerAdapter.supports(mock(HandlerExecution.class));
        assertThat(actual).isFalse();
    }

    @DisplayName("Controller를 실행시킨다.")
    @Test
    void handle() throws Exception {
        final Controller controller = mock(Controller.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(controller.execute(request, response))
                .thenReturn("/index.jsp");

        controllerAdapter.handle(request, response, controller);
        verify(controller)
                .execute(request, response);
    }
}
