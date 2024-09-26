package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerExecutionHandlerAdapterTest {

    private HandlerExecutionHandlerAdapter handlerAdapter;

    @BeforeEach
    public void setUp() {
        handlerAdapter = new HandlerExecutionHandlerAdapter();
    }

    @DisplayName("HandlerExecution을 지원한다.")
    @Test
    void support_true() {
        // given
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        // when
        boolean actual = handlerAdapter.support(handlerExecution);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("HandlerExecution이 아니면 지원하지 않는다.")
    @Test
    void support_false() {
        // given
        Controller controller = mock(Controller.class);

        // when
        boolean actual = handlerAdapter.support(controller);

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("ModelAndView를 반환한다.")
    @Test
    void handle() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        View view = mock(View.class);

        ModelAndView expectedModelAndView = new ModelAndView(view);
        when(handlerExecution.handle(request, response)).thenReturn(expectedModelAndView);

        // when
        ModelAndView actual = handlerExecution.handle(request, response);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual).isInstanceOf(ModelAndView.class);
    }
}
