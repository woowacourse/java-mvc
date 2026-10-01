package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ControllerHandlerAdapterTest {

    private ControllerHandlerAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ControllerHandlerAdapter();
    }

    @Test
    void Controller_구현체는_지원한다() {
        final Controller controller = (request, response) -> "/index.jsp";

        assertThat(adapter.supports(controller)).isTrue();
    }

    @Test
    void Controller_구현체가_아니면_지원하지_않는다() {
        assertThat(adapter.supports(new Object())).isFalse();
    }

    @Test
    void 컨트롤러를_실행하고_반환한_뷰_이름으로_JspView를_담은_ModelAndView를_반환한다() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final AtomicBoolean executed = new AtomicBoolean(false);
        final Controller controller = (req, res) -> {
            executed.set(true);
            return "/index.jsp";
        };

        final ModelAndView modelAndView = adapter.handle(request, response, controller);

        assertThat(executed).isTrue();
        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
    }
}
