package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExecutionAdapterTest {

    private final HandlerExecutionAdapter adapter = new HandlerExecutionAdapter();

    @Test
    void 어노테이션_핸들러만_지원한다() {
        // given
        Class<?> executionType = HandlerExecution.class;

        // when
        boolean supportsExecution = adapter.supports(executionType);
        boolean supportsController = adapter.supports(ForwardController.class);

        // then
        assertThat(supportsExecution).isTrue();
        assertThat(supportsController).isFalse();
    }

    @Test
    void 핸들러가_반환한_ModelAndView를_그대로_전달한다() throws Exception {
        // given
        HandlerExecution execution = mock(HandlerExecution.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ModelAndView expected = new ModelAndView(new JspView("/index.jsp"));
        when(execution.handle(request, response)).thenReturn(expected);

        // when
        ModelAndView actual = adapter.handle(execution, request, response);

        // then
        assertThat(actual).isSameAs(expected);
    }
}
