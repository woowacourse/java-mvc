package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new HandlerAdapterRegistry();
        registry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
    }

    @Test
    void 핸들러를_지원하는_어댑터를_반환한다() throws Exception {
        final var method = TestController.class.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        final var handlerExecution = new HandlerExecution(new TestController(), method);

        assertThat(registry.getHandlerAdapter(handlerExecution)).isInstanceOf(HandlerExecutionHandlerAdapter.class);
    }

    @Test
    void 핸들러를_지원하는_어댑터가_없으면_예외가_발생한다() {
        assertThatThrownBy(() -> registry.getHandlerAdapter(new Object()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("지원하지 않는 핸들러");
    }
}
