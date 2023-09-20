package webmvc.org.springframework.web.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.asis.ControllerHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry registry;
    private HandlerAdapter controllerHandlerAdapter;
    private HandlerAdapter handlerExecutionHandlerAdapter;
    private HandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        controllerHandlerAdapter = mock(ControllerHandlerAdapter.class);
        handlerExecutionHandlerAdapter = mock(HandlerExecutionHandlerAdapter.class);
        handlerAdapter = mock(HandlerAdapter.class);

        registry = new HandlerAdapterRegistry(controllerHandlerAdapter, handlerExecutionHandlerAdapter);
    }

    @Test
    void addHandlerAdapter() {
        // given
        // when
        registry.addHandlerAdapter(handlerAdapter);

        when(handlerAdapter.canHandle(any())).thenReturn(true);
        // then
        assertThat(registry.getHandlerAdapter(new Object())).isEqualTo(handlerAdapter);
    }

    @Test
    void getHandlerAdapter_success() {
        // given
        Object handler = new Object();
        when(controllerHandlerAdapter.canHandle(handler)).thenReturn(true);

        // when
        HandlerAdapter result = registry.getHandlerAdapter(handler);

        // then
        assertThat(result).isEqualTo(controllerHandlerAdapter);
    }

    @Test
    void getHandlerAdapter_fail() {
        // given
        Object handler = new Object();
        when(handlerExecutionHandlerAdapter.canHandle(handler)).thenReturn(false);

        // when
        // then
        assertThatThrownBy(() -> registry.getHandlerAdapter(handler))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("%s에 적절한 어댑터를 찾을 수 없습니다.", handler));

    }
}
