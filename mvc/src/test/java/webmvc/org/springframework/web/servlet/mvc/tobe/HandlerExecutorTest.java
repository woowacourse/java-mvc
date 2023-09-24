package webmvc.org.springframework.web.servlet.mvc.tobe;

import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.tobe.handlerexecution.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.view.ModelAndView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExecutorTest {

    @Test
    void executeIfHandlerExecution() throws Exception {
        //given
        HandlerExecutor handlerExecutor = mock(HandlerExecutor.class);
        ModelAndView modelAndView = mock(ModelAndView.class);

        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        when(handlerExecutor.execute(any(), any(), any())).thenReturn(modelAndView);

        //when, then
        assertThat(handlerExecutor.execute(null, null, handlerExecution))
                .isInstanceOf(ModelAndView.class);
    }

    @Test
    void executeIfNoneHandler() {
        //given
        HandlerExecutor handlerExecutor = new HandlerExecutor();

        //when, then
        assertThatThrownBy(() -> handlerExecutor.execute(null, null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
