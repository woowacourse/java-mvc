package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

class HandlerExecutionAdapterTest {

    @Test
    void 핸들러의_실행결과를_그대로_반환한다() {
        // given
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        ModelAndView expected = new ModelAndView(new JspView("/index.jsp"));
        expected.addObject("id", "gugu");

        when(handlerExecution.handle(request, response)).thenReturn(expected);

        HandlerExecutionAdapter adapter = new HandlerExecutionAdapter();

        // when
        ModelAndView actual = adapter.adapt(request, response, handlerExecution);

        // then
        assertThat(actual).isSameAs(expected);
        verify(handlerExecution).handle(request, response);
    }
}