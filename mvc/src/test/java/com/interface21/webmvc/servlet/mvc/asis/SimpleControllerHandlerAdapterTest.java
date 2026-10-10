package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.HandlerAdapter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SimpleControllerHandlerAdapterTest {

    private final HandlerAdapter handlerAdapter = new SimpleControllerHandlerAdapter();

    @Test
    void 기존_컨트롤러만_지원한다() {
        final Controller controller = (request, response) -> "/index.jsp";

        assertThat(handlerAdapter.supports(controller)).isTrue();
        assertThat(handlerAdapter.supports(new Object())).isFalse();
        assertThat(handlerAdapter.supports(null)).isFalse();
    }

    @Test
    void 기존_컨트롤러의_뷰_이름을_모델앤뷰로_변환해_JSP로_전달한다() throws Exception {
        final Controller controller = (request, response) -> "/index.jsp";
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        final var modelAndView = handlerAdapter.handle(request, response, controller);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        assertThat(modelAndView.getModel()).isEmpty();
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 기존_컨트롤러의_리다이렉트_뷰_이름으로_이동한다() throws Exception {
        final Controller controller = (request, response) -> "redirect:/login";
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final var modelAndView = handlerAdapter.handle(request, response, controller);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(response).sendRedirect("/login");
    }
}
