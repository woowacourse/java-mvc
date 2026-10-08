package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.mvc.exception.AdapterNotFoundException;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.RequestMappingHandlerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry registry;
    private HandlerExecution handlerExecution;

    @BeforeEach
    void setUp() throws Exception {
        registry = new HandlerAdapterRegistry();
        final Method method = TestController.class.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        handlerExecution = new HandlerExecution(new TestController(), method);
    }

    @Test
    void 지원하지_않는_어댑터는_건너뛰고_지원하는_어댑터를_반환한다() {
        final HandlerAdapter unsupportedAdapter = mock(HandlerAdapter.class);
        registry.addHandlerAdapter(unsupportedAdapter);
        registry.addHandlerAdapter(new RequestMappingHandlerAdapter());

        assertThat(registry.getHandlerAdapter(handlerExecution)).isInstanceOf(RequestMappingHandlerAdapter.class);
    }

    @Test
    void 지원하는_어댑터가_여러_개면_먼저_등록된_어댑터를_반환한다() {
        final HandlerAdapter firstAdapter = new RequestMappingHandlerAdapter();
        final HandlerAdapter secondAdapter = new RequestMappingHandlerAdapter();
        registry.addHandlerAdapter(firstAdapter);
        registry.addHandlerAdapter(secondAdapter);

        assertThat(registry.getHandlerAdapter(handlerExecution)).isSameAs(firstAdapter);
    }

    @Test
    void 지원하는_어댑터가_없으면_예외가_발생한다() {
        final Object unsupportedHandler = new Object();
        registry.addHandlerAdapter(new RequestMappingHandlerAdapter());

        assertThatThrownBy(() -> registry.getHandlerAdapter(unsupportedHandler))
                .isInstanceOf(AdapterNotFoundException.class)
                .hasMessageContaining(Object.class.getName());
    }

    @Test
    void 등록된_어댑터가_없으면_예외가_발생한다() {
        assertThatThrownBy(() -> registry.getHandlerAdapter(handlerExecution))
                .isInstanceOf(AdapterNotFoundException.class);
    }
}
