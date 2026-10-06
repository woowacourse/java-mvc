package com.interface21.webmvc.servlet.mvc.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

public class ControllerHandlerAdapterTest {

    private final ControllerHandlerAdapter adapter = new ControllerHandlerAdapter();

    @Test
    void Controller_구현체를_지원한다() {
        final Controller controller = ((req, res) -> "/index.jsp");

        assertThat(adapter.supports(controller)).isTrue();
    }

    @Test
    void Controller가_아니면_지원하지_않는다() {
        assertThat(adapter.supports("test")).isFalse();
    }

    @Test
    void 뷰_이름을_ModelAndView로_감싸서_반환한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final Controller controller = (req, res) -> "/index.jsp";

        final ModelAndView modelAndView = adapter.handle(request, response, controller);

        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
    }
}
