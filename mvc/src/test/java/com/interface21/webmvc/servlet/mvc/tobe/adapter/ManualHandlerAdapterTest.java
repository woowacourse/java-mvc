package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.exception.UnprocessableHandlerException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import samples.TestInterfaceController;

class ManualHandlerAdapterTest {

    @Test
    void 지원_가능한_핸들러일_경우_true_반환() {
        // given
        Controller controller = new TestInterfaceController();
        ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

        // when
        boolean actual = manualHandlerAdapter.supports(controller);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 지원_불가능한_핸들러일_경우_false_반환() {
        // given
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

        // when
        boolean actual = manualHandlerAdapter.supports(handlerExecution);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 핸들러를_실행하고_결과를_반환() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Controller controller = new TestInterfaceController();

        ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

        // when
        ModelAndView actual = manualHandlerAdapter.handle(request, response, controller);

        // then
        assertThat(actual).isNotNull();
    }

    @Test
    void 지원_불가능한_핸들러를_실행할_경우_예외_발생() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

        // when, then
        assertThatThrownBy(() -> manualHandlerAdapter.handle(request, response, handlerExecution))
                .isInstanceOf(UnprocessableHandlerException.class);
    }
}
