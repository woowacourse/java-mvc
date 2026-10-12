package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ControllerHandlerAdapterTest {

    private final ControllerHandlerAdapter handlerAdapter = new ControllerHandlerAdapter();

    @Test
    void Controller_구현체를_지원한다() {
        assertThat(handlerAdapter.supports(new ForwardController("/index.jsp"))).isTrue();
    }

    @Test
    void Controller_구현체가_아니면_지원하지_않는다() {
        assertThat(handlerAdapter.supports(new Object())).isFalse();
    }

    @Test
    void 컨트롤러가_반환한_뷰_이름으로_JspView를_담은_ModelAndView를_반환한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final var modelAndView = handlerAdapter.handle(request, response, new ForwardController("/index.jsp"));

        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
        assertThat(modelAndView.getModel()).isEmpty();
    }
}
