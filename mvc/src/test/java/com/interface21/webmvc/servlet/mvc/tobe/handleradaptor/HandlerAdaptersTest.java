package com.interface21.webmvc.servlet.mvc.tobe.handleradaptor;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.bean.container.BeanContainer;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdaptersTest {

    @BeforeEach
    void setUp() {
        BeanContainer.getInstance().clear();
    }

    @DisplayName("Handler 를 처리할 수 있는 HandlerAdaptor 를 찾는다.")
    @Test
    void findHandlerAdaptor() {
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.registerBeans(List.of(new TestFailHandlerAdapter(), new TestSuccessHandlerAdapter()));

        HandlerAdapters handlerAdapters = new HandlerAdapters();

        HandlerAdapter handlerAdapter = handlerAdapters.findHandlerAdaptor("abc");

        assertThat(handlerAdapter)
                .isInstanceOf(TestSuccessHandlerAdapter.class);
    }

    private static class TestFailHandlerAdapter implements HandlerAdapter {

        @Override
        public boolean support(Object handler) {
            return false;
        }

        @Override
        public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            return null;
        }
    }

    private static class TestSuccessHandlerAdapter implements HandlerAdapter {

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
