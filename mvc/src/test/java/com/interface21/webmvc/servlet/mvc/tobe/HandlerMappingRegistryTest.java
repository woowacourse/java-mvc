package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class HandlerMappingRegistryTest {

    @DisplayName("HandlerMapping에 등록된 순서대로 핸들러 검사를 수행한다.")
    @Test
    void getHandler() {
        // given
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(new FirstHandlerMapping());
        handlerMappingRegistry.addHandlerMapping(new SecondHandlerMapping());

        // when
        String handler = (String) handlerMappingRegistry.getHandler(null);

        // then
        assertThat(handler).isEqualTo("firstHandler");
    }

    static class FirstHandlerMapping implements HandlerMapping {

        @Override
        public void initialize() {
        }

        @Override
        public Object getHandler(HttpServletRequest request) {
            return "firstHandler";
        }

    }

    static class SecondHandlerMapping implements HandlerMapping {

        @Override
        public void initialize() {
        }

        @Override
        public Object getHandler(HttpServletRequest request) {
            return "secondHandler";
        }

    }

    @DisplayName("HandlerMapping에 등록된 핸들러로 처리할 수 없는 request가 들어오면 예외가 발생한다.")
    @Test
    void getHandlerFail() {
        // given
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/test");

        // when & then
        assertThatThrownBy(() -> handlerMappingRegistry.getHandler(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("HttpServletRequest에 대응하는 handler를 찾을 수 없습니다. requestURI = /test");
    }
}
