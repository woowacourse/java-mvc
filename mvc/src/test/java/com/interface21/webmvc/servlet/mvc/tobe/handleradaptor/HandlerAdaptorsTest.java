package com.interface21.webmvc.servlet.mvc.tobe.handleradaptor;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.bean.container.BeanContainer;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdaptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdaptorsTest {

    @BeforeEach
    void setUp() {
        BeanContainer.getInstance().clear();
    }

    @DisplayName("Handler 를 처리할 수 있는 HandlerAdaptor 를 찾는다.")
    @Test
    void findHandlerAdaptor() {
        HandlerAdaptors handlerAdaptors = new HandlerAdaptors();

        HandlerAdaptor handlerAdaptor = handlerAdaptors.findHandlerAdaptor("abc");

        assertThat(handlerAdaptor)
                .isInstanceOf(TestSuccessHandlerAdaptor.class);
    }

    private static class TestFailHandlerAdaptor implements HandlerAdaptor {

        @Override
        public boolean support(Object handler) {
            return false;
        }

        @Override
        public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            return null;
        }
    }

    private static class TestSuccessHandlerAdaptor implements HandlerAdaptor {

        @Override
        public boolean support(Object handler) {
            return true;
        }

        @Override
        public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            return null;
        }
    }
}
