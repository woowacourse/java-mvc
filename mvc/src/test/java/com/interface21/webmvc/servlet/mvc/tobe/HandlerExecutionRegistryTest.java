package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.web.bind.annotation.RequestMethod;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerExecutionRegistryTest {

    static class RegistryTestController {
        public void testHandler() {
        }
    }

    @Test
    @DisplayName("핸들러를 성공적으로 등록하고 조회한다.")
    void registerHandler() throws Exception {
        HandlerExecutionRegistry registry = new HandlerExecutionRegistry();
        Method handlerMethod = RegistryTestController.class.getMethod("testHandler");
        registry.registerHandler(RequestMethod.GET, "/test", handlerMethod);

        HandlerExecution handlerExecution = (HandlerExecution) registry.getHandler(RequestMethod.GET, "/test");
        assertThat(handlerExecution).isNotNull();
    }

    @Test
    @DisplayName("중복된 핸들러를 등록할 수 없다.")
    void duplicateHandler() throws Exception {
        HandlerExecutionRegistry registry = new HandlerExecutionRegistry();
        Method handlerMethod = RegistryTestController.class.getMethod("testHandler");
        registry.registerHandler(RequestMethod.GET, "/test", handlerMethod);

        assertThatThrownBy(() -> registry.registerHandler(RequestMethod.GET, "/test", handlerMethod))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Handler already registered for");
    }
}
