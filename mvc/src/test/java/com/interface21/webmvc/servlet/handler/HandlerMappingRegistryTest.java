package com.interface21.webmvc.servlet.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.webmvc.servlet.exception.NotFoundHandlerException;
import jakarta.servlet.http.HttpServletRequest;
import javax.annotation.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class HandlerMappingRegistryTest {

    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
    }

    private static final String HANDLER = "handler";

    @Test
    void getHandlerTest_whenPossibleToHandle_returnHandler() {
        HandlerMappingRegistry registry = new HandlerMappingRegistry();
        registry.addHandlerMapping(new StringHandlerMapping());
        registry.addHandlerMapping(new NeverHandlerMapping());

        Object actual = registry.getHandler(request);

        assertThat(actual).isEqualTo(HANDLER);
    }

    @Test
    void getHandlerTest_whenImpossibleToHandle_throwException() {
        HandlerMappingRegistry registry = new HandlerMappingRegistry();
        registry.addHandlerMapping(new NeverHandlerMapping());
        registry.addHandlerMapping(new NeverHandlerMapping());

        assertThatThrownBy(() -> registry.getHandler(request))
                .isInstanceOf(NotFoundHandlerException.class);
    }

    private static class StringHandlerMapping implements HandlerMapping {

        @Nullable
        @Override
        public Object getHandler(HttpServletRequest request) {
            return HANDLER;
        }
    }

    private static class NeverHandlerMapping implements HandlerMapping {

        @Nullable
        @Override
        public Object getHandler(HttpServletRequest request) {
            return null;
        }
    }
}
