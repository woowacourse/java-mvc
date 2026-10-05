package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new HandlerAdapterRegistry();
        registry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        registry.addHandlerAdapter(new ControllerHandlerAdapter());
    }

    @Test
    void 핸들러를_지원하는_어댑터를_반환한다() {
        final Controller controller = (request, response) -> "/index.jsp";

        assertThat(registry.getHandlerAdapter(controller)).isInstanceOf(ControllerHandlerAdapter.class);
    }

    @Test
    void 핸들러를_지원하는_어댑터가_없으면_예외가_발생한다() {
        assertThatThrownBy(() -> registry.getHandlerAdapter(new Object()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("지원하지 않는 핸들러");
    }
}
