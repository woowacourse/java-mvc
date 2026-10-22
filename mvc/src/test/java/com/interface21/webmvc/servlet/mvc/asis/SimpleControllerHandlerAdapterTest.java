package com.interface21.webmvc.servlet.mvc.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SimpleControllerHandlerAdapterTest {

    private final SimpleControllerHandlerAdapter adapter = new SimpleControllerHandlerAdapter();

    @Test
    @DisplayName("Legacy Controller만 지원한다")
    void supports_legacy_controller() {
        assertThat(adapter.supports(new ForwardController("/index.jsp"))).isTrue();
        assertThat(adapter.supports(new Object())).isFalse();
        assertThat(adapter.supports(null)).isFalse();
    }

    @Test
    @DisplayName("Legacy 컨트롤러의 문자열 뷰를 JSP로 렌더링한다")
    void forward_legacy_view() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        final var modelAndView = adapter.handle(request, response, new ForwardController("/index.jsp"));
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("Legacy 컨트롤러의 redirect를 유지한다")
    void redirect_legacy_view() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final Controller controller = (req, res) -> "redirect:/index.jsp";

        final var modelAndView = adapter.handle(request, response, controller);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    @DisplayName("Legacy 컨트롤러 실행 실패를 그대로 전달한다")
    void propagate_execution_failure() {
        final var failure = new IllegalStateException("failed");
        final Controller controller = (req, res) -> {
            throw failure;
        };

        assertThatThrownBy(() -> adapter.handle(mock(HttpServletRequest.class),
                mock(HttpServletResponse.class), controller)).isSameAs(failure);
    }
}
