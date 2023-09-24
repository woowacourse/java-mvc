package webmvc.org.springframework.web.servlet.mvc.tobe.handleradapter;

import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.tobe.view.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.handlerexecution.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.handleradapter.HandlerExecutionAdapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExecutionAdapterTest {

    @Test
    void supports() throws Exception {
        //given
        HandlerExecutionAdapter handlerExecutionAdapter = new HandlerExecutionAdapter();
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        //when
        boolean supports = handlerExecutionAdapter.supports(handlerExecution);

        //then
        assertThat(supports).isTrue();
    }

    @Test
    void adapt() throws Exception {
        //given
        HandlerExecutionAdapter executionAdapter = mock(HandlerExecutionAdapter.class);
        ModelAndView modelAndView = mock(ModelAndView.class);

        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        when(executionAdapter.adapt(any(), any(), any())).thenReturn(modelAndView);

        //when, then
        assertThat(executionAdapter.adapt(null, null, handlerExecution))
                .isInstanceOf(ModelAndView.class);
    }
}
