package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LegacyHandlerAdapterTest {

    private final LegacyHandlerAdapter adapter = new LegacyHandlerAdapter();

    @Test
    void supports() {
        final Controller controller = new ForwardController("/index.jsp");

        assertThat(adapter.supports(controller)).isTrue();
        assertThat(adapter.supports(new Object())).isFalse();
    }

    @Test
    void handle_forward() throws Exception {
        final Controller controller = mock(Controller.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        when(controller.execute(request, response)).thenReturn("/index.jsp");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        final ModelAndView mav = adapter.handle(controller, request, response);
        mav.getView().render(mav.getModel(), request, response);

        verify(controller).execute(request, response);
        verify(dispatcher).forward(request, response);
    }

    @Test
    void handle_redirect() throws Exception {
        final Controller controller = mock(Controller.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(controller.execute(request, response))
                .thenReturn("redirect:/index.jsp");

        final ModelAndView mav = adapter.handle(controller, request, response);
        mav.getView().render(mav.getModel(), request, response);

        verify(response).sendRedirect("/index.jsp");
    }
}
