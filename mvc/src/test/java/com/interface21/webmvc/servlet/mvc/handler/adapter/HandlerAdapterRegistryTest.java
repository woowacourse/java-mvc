package com.interface21.webmvc.servlet.mvc.handler.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.ModelAndView;

class HandlerAdapterRegistryTest {

    @DisplayName("HandlerAdapter에 등록된 순서대로 어댑터 검사를 수행한다.")
    @Test
    void getHandlerAdapter() {
        // given
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new FirstAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new SecondAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new ThirdAdapter());
        Object handler = "testHandler";

        // when
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

        // then
        assertThat(handlerAdapter).isInstanceOf(SecondAdapter.class);
    }

    static class FirstAdapter implements HandlerAdapter {

        @Override
        public boolean canHandle(Object handler) {
            return false;
        }

        @Override
        public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            return null;
        }
    }

    static class SecondAdapter implements HandlerAdapter {

        @Override
        public boolean canHandle(Object handler) {
            return true;
        }

        @Override
        public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            return null;
        }
    }

    static class ThirdAdapter implements HandlerAdapter {

        @Override
        public boolean canHandle(Object handler) {
            return true;
        }

        @Override
        public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            return null;
        }
    }

    @DisplayName("HandlerAdapter에 등록된 어댑터로 처리할 수 없는 핸들러가 들어오면 예외가 발생한다.")
    @Test
    void getHandlerAdapterFail() {
        // given
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        Object handler = "testHandler";

        // when & then
        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(handler))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("handler를 처리할 adapter가 존재하지 않습니다. handler = java.lang.String");
    }
}
