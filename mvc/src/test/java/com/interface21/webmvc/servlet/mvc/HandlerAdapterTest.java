package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

class HandlerAdapterTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @Test
    void annotationAdapter는_HandlerExecution만_지원한다() {
        final var adapter = new AnnotationHandlerAdapter();

        assertThat(adapter.supports(mock(HandlerExecution.class))).isTrue();
        assertThat(adapter.supports(new Object())).isFalse();
    }

    @Test
    void annotationAdapter는_HandlerExecution_결과를_그대로_반환한다() throws Exception {
        final var execution = mock(HandlerExecution.class);
        final var expected = new ModelAndView(new JspView("/index.jsp"));
        when(execution.handle(request, response)).thenReturn(expected);

        final var mav = new AnnotationHandlerAdapter().handle(request, response, execution);

        assertThat(mav).isSameAs(expected);
    }
}
