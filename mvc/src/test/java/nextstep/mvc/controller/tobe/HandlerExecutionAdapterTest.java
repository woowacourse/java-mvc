package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.asis.ForwardController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerExecutionAdapterTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @Test
    @DisplayName("HandlerExecutionAdapter에서 지원하지 않는 경우 handler일 경우 false")
    void isFalseSupports() {
        final HandlerExecutionAdapter handlerExecutionAdapter = new HandlerExecutionAdapter();
        final Controller forwardController = new ForwardController("/");

        final boolean actual = handlerExecutionAdapter.supports(forwardController);

        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("HandlerExecutionAdapter에서 지원하는 경우 handler일 경우 true")
    void isTrueSupports() {
        final HandlerExecutionAdapter handlerExecutionAdapter = new HandlerExecutionAdapter();
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final boolean actual = handlerExecutionAdapter.supports(handlerExecution);

        assertThat(actual).isTrue();
    }
}
