package com.interface21.webmvc.servlet.mvc.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ControllerHandlerAdapterTest {

    private final ControllerHandlerAdapter adapter = new ControllerHandlerAdapter();

    @Test
    void supports_WhenHandlerIsController_ThenTrue() {
        Controller controller = mock(Controller.class);

        assertThat(adapter.supports(controller)).isTrue();
    }

    @Test
    void supports_WhenHandlerIsNotController_ThenFalse() {
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        assertThat(adapter.supports(handlerExecution)).isFalse();
        assertThat(adapter.supports(new Object())).isFalse();
        assertThat(adapter.supports(null)).isFalse();
    }

    @Test
    void handle_ThenWrapViewNameWithJspView() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final Controller controller = mock(Controller.class);

        when(controller.execute(request, response)).thenReturn("redirect:/index.jsp");

        final ModelAndView modelAndView = adapter.handle(request, response, controller);

        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
        modelAndView.getView().render(Map.of(), request, response);
        verify(response).sendRedirect("/index.jsp");
    }
}
