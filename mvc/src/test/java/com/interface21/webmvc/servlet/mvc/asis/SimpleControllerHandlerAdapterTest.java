package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class SimpleControllerHandlerAdapterTest {

    private final HandlerAdapter adapter = new SimpleControllerHandlerAdapter();

    @Test
    void supportsOnlyLegacyControllers() {
        assertThat(adapter.supports(mock(Controller.class))).isTrue();
        assertThat(adapter.supports(mock(HandlerExecution.class))).isFalse();
        assertThat(adapter.supports(new Object())).isFalse();
        assertThat(adapter.supports(null)).isFalse();
    }

    @Test
    void convertsViewNameToRenderableModelAndView() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var controller = mock(Controller.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(controller.execute(request, response)).thenReturn("/index.jsp");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        final var result = adapter.handle(request, response, controller);
        result.getView().render(result.getModel(), request, response);

        assertThat(result.getModel()).isEmpty();
        verify(controller).execute(request, response);
        verify(dispatcher).forward(request, response);
    }

    @Test
    void preservesRedirectViewName() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var controller = mock(Controller.class);
        when(controller.execute(request, response)).thenReturn("redirect:/index.jsp");

        final var result = adapter.handle(request, response, controller);
        result.getView().render(result.getModel(), request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void propagatesControllerFailure() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var controller = mock(Controller.class);
        final var failure = new Exception("execution failed");
        when(controller.execute(request, response)).thenThrow(failure);

        assertThatThrownBy(() -> adapter.handle(request, response, controller)).isSameAs(failure);
    }
}
