package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;

class HandlerExecutionHandlerAdapterTest {

    @Test
    void HandlerAdapter가_처리할_수_있는_핸들러이면_true를_반환한다() {
        HandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();
        Object handlerExecution = mock(HandlerExecution.class);

        boolean actual = handlerAdapter.supports(handlerExecution);

        assertThat(actual).isTrue();
    }

    @Test
    void HandlerAdapter가_처리할_수_없는_핸들러면_false를_반환한다() {
        HandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();
        Object notHandlerExecution = mock(Object.class);

        boolean actual = handlerAdapter.supports(notHandlerExecution);

        assertThat(actual).isFalse();
    }

    @Test
    void Handler로_HandlerExecution이_들어오면_처리하여_ModelAndView를_반환한다() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerExecution handler = mock(HandlerExecution.class);

        ModelAndView modelAndView = new ModelAndView(new JspView(""));
        HandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();

        when(handler.handle(request, response)).thenReturn(modelAndView);

        ModelAndView actual = handlerAdapter.handle(request, response, handler);

        assertThat(actual).isEqualTo(modelAndView);
    }
}
