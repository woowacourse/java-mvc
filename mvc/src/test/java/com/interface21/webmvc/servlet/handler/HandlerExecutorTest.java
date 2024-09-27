package com.interface21.webmvc.servlet.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.exception.NotFoundHandlerAdapterException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class HandlerExecutorTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private Object handler;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        handler = new Object();
    }

    @Test
    void handleTest_whenAdapterCanHandle_returnModelAndView() throws Exception {
        HandlerExecutor handlerExecutor = new HandlerExecutor(
                List.of(new NeverHandlerAdapter(), new AlwaysHandlerAdapter()));

        ModelAndView modelAndView = handlerExecutor.handle(request, response, handler);

        assertThat(modelAndView).isNull();
    }

    @Test
    void handleTest_whenNoAdapterCanHandle_throwException() {
        HandlerExecutor handlerExecutor = new HandlerExecutor(
                List.of(new NeverHandlerAdapter(), new NeverHandlerAdapter()));

        assertThatThrownBy(() -> handlerExecutor.handle(request, response, handler))
                .isInstanceOf(NotFoundHandlerAdapterException.class);
    }

    private static class AlwaysHandlerAdapter implements HandlerAdapter {

        @Override
        public boolean canHandle(Object handler) {
            return true;
        }

        @Override
        public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {
            return null;
        }
    }

    private static class NeverHandlerAdapter implements HandlerAdapter {

        @Override
        public boolean canHandle(Object handler) {
            return false;
        }

        @Override
        public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {
            return null;
        }
    }
}
