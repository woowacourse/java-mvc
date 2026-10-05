package com.interface21.webmvc.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ControllerHandlerAdapterTest {

    private final ControllerHandlerAdapter handlerAdapter = new ControllerHandlerAdapter();

    @Test
    void Controller_타입의_핸들러만_지원한다() {
        final Controller controller = (request, response) -> "/index.jsp";

        assertThat(handlerAdapter.supports(controller)).isTrue();
        assertThat(handlerAdapter.supports(new Object())).isFalse();
    }

    @Test
    void 컨트롤러가_반환한_뷰_이름으로_ModelAndView를_만든다() throws Exception {
        final Controller controller = (request, response) -> "redirect:/index.jsp";
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final var modelAndView = handlerAdapter.handle(request, response, controller);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(response).sendRedirect("/index.jsp");
    }
}
