package com.interface21.webmvc.servlet.mvc.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ControllerHandlerAdapterTest {

    private final ControllerHandlerAdapter handlerAdapter = new ControllerHandlerAdapter();

    @Test
    void supports() {
        assertThat(handlerAdapter.supports(new ForwardController("/index.jsp"))).isTrue();
        assertThat(handlerAdapter.supports(new Object())).isFalse();
    }

    @Test
    void handle() throws Exception {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        //when
        final var modelAndView = handlerAdapter.handle(request, response, new ForwardController("redirect:/index.jsp"));
        modelAndView.getView().render(Map.of(), request, response);

        //then
        verify(response).sendRedirect("/index.jsp");
    }
}
