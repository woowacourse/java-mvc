package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class AnnotationHandlerRegistrationTest {

    private Map<HandlerKey, HandlerExecution> handlerExecutions;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        final var handlerMapping = new AnnotationHandlerMapping("registration");
        handlerMapping.initialize();
        handlerExecutions = (Map<HandlerKey, HandlerExecution>) ReflectionTestUtils.getField(
                handlerMapping, "handlerExecutions");
    }

    @Test
    void registersDifferentHandlersForSameUrl() throws Exception {
        final var getHandler = handlerExecutions.get(new HandlerKey("/same", RequestMethod.GET));
        final var postHandler = handlerExecutions.get(new HandlerKey("/same", RequestMethod.POST));

        assertThat(getHandler).isNotSameAs(postHandler);
        assertThat(getHandler.handle(mock(HttpServletRequest.class), mock(HttpServletResponse.class))
                .getObject("method")).isEqualTo("get");
        assertThat(postHandler.handle(mock(HttpServletRequest.class), mock(HttpServletResponse.class))
                .getObject("method")).isEqualTo("post");
        assertThat(handlerExecutions).doesNotContainKey(new HandlerKey("/same", RequestMethod.PUT));
    }

    @Test
    void registersAllSpecifiedMethods() throws Exception {
        final var getHandler = handlerExecutions.get(new HandlerKey("/multiple", RequestMethod.GET));
        final var postHandler = handlerExecutions.get(new HandlerKey("/multiple", RequestMethod.POST));

        assertThat(getHandler).isNotNull().isSameAs(postHandler);
        assertThat(getHandler.handle(mock(HttpServletRequest.class), mock(HttpServletResponse.class))
                .getObject("method")).isEqualTo("multiple");
        assertThat(handlerExecutions).doesNotContainKey(new HandlerKey("/multiple", RequestMethod.DELETE));
    }

    @Test
    void registersAllMethodsWhenMethodIsOmitted() throws Exception {
        for (RequestMethod requestMethod : RequestMethod.values()) {
            final var handler = handlerExecutions.get(new HandlerKey("/all", requestMethod));

            assertThat(handler).isNotNull();
            assertThat(handler.handle(mock(HttpServletRequest.class), mock(HttpServletResponse.class))
                    .getObject("method")).isEqualTo("all");
        }
    }

    @Test
    void excludesMethodsWithoutRequestMapping() {
        assertThat(handlerExecutions).hasSize(4 + RequestMethod.values().length);
        for (RequestMethod requestMethod : RequestMethod.values()) {
            assertThat(handlerExecutions).doesNotContainKey(new HandlerKey("", requestMethod));
        }
    }
}
