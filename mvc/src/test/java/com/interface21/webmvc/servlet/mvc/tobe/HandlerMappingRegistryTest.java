package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class HandlerMappingRegistryTest {

    @Test
    @DisplayName("적절한 핸들러를 찾을 수 있는지 테스트")
    void findHandler() {
        // given
        HandlerMappingRegistry registry = new HandlerMappingRegistry();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/test");

        TestHandlerMapping testHandlerMapping = new TestHandlerMapping("/test", new Object());
        registry.add(testHandlerMapping);

        // when
        Object handler = registry.findHandler(request);

        // then
        assertThat(handler).isEqualTo(testHandlerMapping.getHandler(request));
    }

    @Test
    @DisplayName("적절한 핸들러가 없을 때 예외를 던지는지 테스트")
    void findHandler_Failure() {
        // given
        HandlerMappingRegistry registry = new HandlerMappingRegistry();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/non-existent");

        // when & then
        assertThatThrownBy(() -> registry.findHandler(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("요청에 알맞는 Handler를 찾을 수 없습니다.");
    }

    static class TestHandlerMapping implements HandlerMapping {

        private final String supportedUri;
        private final Object handler;

        public TestHandlerMapping(String supportedUri, Object handler) {
            this.supportedUri = supportedUri;
            this.handler = handler;
        }

        @Override
        public Object getHandler(HttpServletRequest request) {
            if (supportedUri.equals(request.getRequestURI())) {
                return handler;
            }
            return null;
        }
    }
}
