package com.interface21.webmvc.servlet.mvc.asis;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ControllerHandlerAdapterTest {

    private final ControllerHandlerAdapter handlerAdapter = new ControllerHandlerAdapter();

    @Test
    void Controller_구현체를_지원한다() {
        assertThat(handlerAdapter.supports(new ForwardController("/index.jsp"))).isTrue();
        assertThat(handlerAdapter.supports(new Object())).isFalse();
    }

    @Test
    void Controller가_반환한_뷰_이름으로_ModelAndView를_만든다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        final var modelAndView = handlerAdapter.handle(request, response, new ForwardController("/index.jsp"));
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(dispatcher).forward(request, response);
    }
}
