package com.interface21.webmvc.servlet.mvc.tobe.handlermapping;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import samples.TestController;

class HandlerMappingsTest {

    @DisplayName("요청을 처리할 수 있는 HandlerMapping 을 찾는다.")
    @Test
    void findHandler() {
        HandlerMappings handlerMappings = new HandlerMappings(
                new TestFailHandlerMappings(), new TestSuccessHandlerMappings());
        HttpServletRequest request = new MockHttpServletRequest();
        Object handler = handlerMappings.findHandler(request);

        assertThat(handler)
                .isInstanceOf(HandlerExecution.class);
    }

    private static class TestFailHandlerMappings implements HandlerMapping {

        @Override
        public void initialize() {
        }

        @Override
        public Object getHandler(HttpServletRequest request) {
            throw new IllegalArgumentException();
        }
    }

    private static class TestSuccessHandlerMappings implements HandlerMapping {

        @Override
        public void initialize() {
        }

        @Override
        public Object getHandler(HttpServletRequest request) {
            Method method = TestController.class.getMethods()[0];
            return new HandlerExecution(method);
        }
    }
}
