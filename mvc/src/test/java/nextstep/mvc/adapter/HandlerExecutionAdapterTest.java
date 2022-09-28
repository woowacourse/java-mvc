package nextstep.mvc.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerExecutionAdapterTest {

    private final HandlerExecutionAdapter handlerExecutionAdapter = new HandlerExecutionAdapter();

    @DisplayName("Handler가 HandlerExecution의 구현체이면 지원한다.")
    @Test
    void supports_true() {
        final boolean actual = handlerExecutionAdapter.supports(mock(HandlerExecution.class));
        assertThat(actual).isTrue();
    }

    @DisplayName("HandlerExecution을 실행시킨다.")
    @Test
    void handle() throws Exception {
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(handlerExecution.handle(request, response))
                .thenReturn(new ModelAndView(new JspView("")));

        handlerExecutionAdapter.handle(request, response, handlerExecution);
        verify(handlerExecution)
                .handle(request, response);
    }
}
