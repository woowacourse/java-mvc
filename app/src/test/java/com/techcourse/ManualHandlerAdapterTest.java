package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;

class ManualHandlerAdapterTest {

    @Test
    void HandlerAdapter가_처리할_수_있는_핸들러이면_true를_반환한다() {
        HandlerAdapter handlerAdapter = new ManualHandlerAdapter();
        Controller controller = mock(Controller.class);

        boolean actual = handlerAdapter.supports(controller);

        assertThat(actual).isTrue();
    }

    @Test
    void HandlerAdapter가_처리할_수_없는_핸들러이면_false를_반환한다() {
        HandlerAdapter handlerAdapter = new ManualHandlerAdapter();
        HandlerExecution notController = mock(HandlerExecution.class);

        boolean actual = handlerAdapter.supports(notController);

        assertThat(actual).isFalse();
    }

    @Test
    void handler가_반환한_viewName의_JSP뷰를_반환한다() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Controller handler = mock(Controller.class);

        HandlerAdapter handlerAdapter = new ManualHandlerAdapter();

        when(handler.execute(request, response)).thenReturn("viewName");

        ModelAndView actual = handlerAdapter.handle(request, response, handler);

        assertThat(actual).isNotNull();
    }
}
