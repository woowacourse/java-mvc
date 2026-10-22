package com.interface21.webmvc.servlet.mvc.asis;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SimpleControllerHandlerAdapterTest {

    private final SimpleControllerHandlerAdapter adapter = new SimpleControllerHandlerAdapter();

    @Test
    void Controller_인터페이스를_구현한_핸들러만_지원한다() {
        assertThat(adapter.supports(new ForwardController("/index.jsp"))).isTrue();
        assertThat(adapter.supports(new Object())).isFalse();
        assertThat(adapter.supports(null)).isFalse();
    }

    @Test
    void 기존_컨트롤러의_실행_결과를_JSP_뷰로_변환한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        final var controller = mock(Controller.class);
        when(controller.execute(request, response)).thenReturn("/legacy.jsp");
        when(request.getRequestDispatcher("/legacy.jsp")).thenReturn(dispatcher);

        final var modelAndView = adapter.handle(request, response, controller);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(controller).execute(request, response);
        verify(dispatcher).forward(request, response);
    }

    @Test
    void 기존_컨트롤러의_redirect_실행_결과를_유지한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final var modelAndView = adapter.handle(request, response, new ForwardController("redirect:/login"));
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(response).sendRedirect("/login");
    }
}
