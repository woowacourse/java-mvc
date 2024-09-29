package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdaptorRegistryTest {


    @Test
    @DisplayName("적절한 핸들러 어댑터를 찾을 수 있는지 테스트")
    void findHandlerAdaptor() {
        // given
        HandlerAdaptorRegistry registry = new HandlerAdaptorRegistry();
        TestHandlerAdaptor testHandlerAdaptor = new TestHandlerAdaptor();
        registry.add(testHandlerAdaptor);
        HandlerExecution supportedHandler = new HandlerExecution(null);

        // when
        HandlerAdaptor foundAdaptor = registry.findHandlerAdaptor(supportedHandler);

        // then
        assertThat(foundAdaptor).isEqualTo(testHandlerAdaptor);
    }

    @Test
    @DisplayName("적절한 핸들러 어댑터가 없을 때 예외를 던지는지 테스트")
    void findHandlerAdaptor_Failure() {
        // given
        HandlerAdaptorRegistry registry = new HandlerAdaptorRegistry();
        Object unsupportedHandler = new Object(); // 테스트 핸들러

        // when & then
        assertThatThrownBy(() -> registry.findHandlerAdaptor(unsupportedHandler))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("요청에 알맞는 Handler Adaptor를 찾을 수 없습니다.");
    }

    static class TestHandlerAdaptor implements HandlerAdaptor {

        @Override
        public boolean isSupported(Object handler) {
            return handler instanceof HandlerExecution;
        }

        @Override
        public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            return null;
        }
    }
}
