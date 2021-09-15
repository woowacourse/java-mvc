package nextstep.mvc.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.ManualController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

@DisplayName("HandlerAdapter 기능 테스트")
class HandlerAdapterTest {

    @Test
    void annotationHandlerAdapterTest() {
        //given
        final HandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);
        //when
        willReturn("get-test-annotation").given(handlerExecution).handle(any(HttpServletRequest.class),
                any(HttpServletResponse.class), any(ModelAndView.class));
        final boolean isSupport = handlerAdapter.supports(handlerExecution);
        //then
        assertThat(isSupport).isTrue();
    }

    @Test
    void manualHandlerAdapterTest() {
        //given
        final HandlerAdapter handlerAdapter = new ControllerHandlerAdapter();
        final ManualController manualController = new ManualController();
        //when
        final boolean isSupport = handlerAdapter.supports(manualController);
        //then
        assertThat(isSupport).isTrue();
    }
}