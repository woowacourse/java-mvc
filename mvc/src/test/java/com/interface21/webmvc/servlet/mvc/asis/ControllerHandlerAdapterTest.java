package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ControllerHandlerAdapterTest {

    private final ControllerHandlerAdapter adapter = new ControllerHandlerAdapter();

    @DisplayName("레거시 Controller만 지원한다")
    @Test
    void supportsOnlyLegacyController() {
        final Controller legacyController = (request, response) -> "/index.jsp";

        assertThat(adapter.supports(legacyController)).isTrue();
        assertThat(adapter.supports(mock(HandlerExecution.class))).isFalse();
        assertThat(adapter.supports(new Object())).isFalse();
    }

    @DisplayName("Controller가 반환한 뷰 이름을 JspView로 감싼 ModelAndView로 바꾼다")
    @Test
    void wrapsViewNameIntoModelAndView() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);
        final Controller legacyController = (req, res) -> "/index.jsp";

        final var modelAndView = adapter.handle(request, response, legacyController);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        assertThat(modelAndView.getModel()).isEmpty();
        verify(dispatcher).forward(request, response);
    }
}