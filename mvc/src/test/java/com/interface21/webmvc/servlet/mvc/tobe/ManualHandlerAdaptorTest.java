package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import samples.ManualTestController;

class ManualHandlerAdaptorTest {

    private ManualHandlerAdaptor handlerAdaptor;

    @BeforeEach
    void setUp() {
        handlerAdaptor = new ManualHandlerAdaptor();
    }

    @Test
    @DisplayName("Controller 타입의 핸들러를 지원하는 경우 true를 반환한다.")
    void isSupported_withHandlerExecution() {
        // given
        Controller manualTestController = new ManualTestController();

        // when
        boolean result = handlerAdaptor.isSupported(manualTestController);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("HandlerExecution 타입의 핸들러를 지원하는 경우 false를 반환한다.")
    @Test
    void methodName() {
        // given
        Object otherHandler = new Object();

        // when
        boolean result = handlerAdaptor.isSupported(otherHandler);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("handle은 Controller의 요청을 처리하고 view 경로를 반환하는지 확인한다.")
    void handle_withHandlerExecution() {
        // given
        Controller manualTestController = new ManualTestController();
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();

        // when
        ModelAndView modelAndView = handlerAdaptor.handle(request, response, manualTestController);

        // then
        assertThat(modelAndView.getViewName()).isEqualTo("redirect:/index.jsp");
    }
}
