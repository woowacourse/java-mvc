package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerExecutionHandlerAdapterTest {

    @Test
    @DisplayName("요청에 맞는 view를 반환한다.")
    void handle() throws Exception {
        final HandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();
        final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);
        final ModelAndView modelAndView = new ModelAndView(new JspView("test"));
        when(handlerExecution.handle(
                any(HttpServletRequest.class),
                any(HttpServletResponse.class)
        )).thenReturn(modelAndView);

        final ModelAndView actual = handlerAdapter.handle(
                httpServletRequest,
                httpServletResponse,
                handlerExecution);

        assertThat(actual).isEqualTo(modelAndView);
    }
}
