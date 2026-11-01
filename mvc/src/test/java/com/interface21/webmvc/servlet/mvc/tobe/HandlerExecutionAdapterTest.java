package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HandlerExecutionAdapterTest {

    private final HandlerExecutionAdapter adapter = new HandlerExecutionAdapter();

    @Test
    void HandlerExecution_타입의_핸들러만_지원한다() {
        assertThat(adapter.supports(mock(HandlerExecution.class))).isTrue();
        assertThat(adapter.supports(new Object())).isFalse();
        assertThat(adapter.supports(null)).isFalse();
    }

    @Test
    void 요청과_응답을_핸들러에_전달하고_ModelAndView를_그대로_반환한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var handler = mock(HandlerExecution.class);
        final var expected = new ModelAndView(new JspView("/test.jsp")).addObject("id", "gugu");
        when(handler.handle(request, response)).thenReturn(expected);

        assertThat(adapter.handle(request, response, handler)).isSameAs(expected);
        verify(handler).handle(request, response);
    }
}
