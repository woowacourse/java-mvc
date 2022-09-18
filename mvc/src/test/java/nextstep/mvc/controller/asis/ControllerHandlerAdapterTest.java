package nextstep.mvc.controller.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

class ControllerHandlerAdapterTest {

    @Test
    void supportsIsTrue() {
        Controller controller = mock(Controller.class);
        ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();

        boolean result = controllerHandlerAdapter.supports(controller);

        assertThat(result).isTrue();
    }

    @Test
    void supportsIsFalse() {
        ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();

        boolean result = controllerHandlerAdapter.supports(null);

        assertThat(result).isFalse();
    }

    @Test
    void handle() throws Exception {
        Controller controller = mock(Controller.class);
        ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        controllerHandlerAdapter.handle(request, response, controller);

        verify(controller).execute(request, response);
    }
}
