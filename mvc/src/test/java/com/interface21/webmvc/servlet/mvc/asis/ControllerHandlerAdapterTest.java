package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ControllerHandlerAdapterTest {

    private final HandlerAdapter adapter = new ControllerHandlerAdapter();

    @Test
    void supportsOnlyLegacyControllers() {
        assertThat(adapter.supports(mock(Controller.class))).isTrue();
        assertThat(adapter.supports(mock(HandlerExecution.class))).isFalse();
        assertThat(adapter.supports(null)).isFalse();
    }

    @Test
    void convertsControllerViewNameToRenderableModelAndView() throws Exception {
        final var controller = mock(Controller.class);
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(controller.execute(request, response)).thenReturn("/test.jsp");
        when(request.getRequestDispatcher("/test.jsp")).thenReturn(dispatcher);

        final var result = adapter.handle(controller, request, response);

        verify(controller).execute(request, response);
        assertThat(result.getModel()).isEmpty();
        verifyNoInteractions(dispatcher);
        result.getView().render(result.getModel(), request, response);
        verify(dispatcher).forward(request, response);
    }

    @Test
    void preservesRedirectViewName() throws Exception {
        final var controller = mock(Controller.class);
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(controller.execute(request, response)).thenReturn("redirect:/users");

        final var result = adapter.handle(controller, request, response);

        verifyNoInteractions(response);
        result.getView().render(result.getModel(), request, response);
        verify(response).sendRedirect("/users");
    }
}
