package com.techcourse;

import com.techcourse.adapter.ManualHandlerAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

import static org.assertj.core.api.Assertions.assertThat;

class ManualHandlerAdapterTest {
    private final ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

    @Test
    @DisplayName("지원하지 않는 handler 타입이면 false를 반환한다.")
    void supports_fail() {
        //given
        HandlerExecution handlerExecution = new HandlerExecution(null, null);

        //when
        final boolean supports = manualHandlerAdapter.supports(handlerExecution);

        //then
        assertThat(supports).isFalse();
    }

    @Test
    @DisplayName("지원하는 handler 타입이면 true를 반환한다.")
    void supports() {
        //given
        Controller controller = new ForwardController("");

        //when
        final boolean supports = manualHandlerAdapter.supports(controller);

        //then
        assertThat(supports).isTrue();
    }
}
