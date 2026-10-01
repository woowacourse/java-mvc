package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
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

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry registry;
    private Controller controller;
    private HandlerExecution handlerExecution;

    @BeforeEach
    void setUp() throws Exception {
        registry = new HandlerAdapterRegistry();
        controller = (request, response) -> "/index.jsp";
        final Method method = TestController.class.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        handlerExecution = new HandlerExecution(new TestController(), method);
    }

    @Test
    void 레거시_Controller면_ControllerHandlerAdapter를_반환한다() {
        registry.addHandlerAdapter(new RequestMappingHandlerAdapter());
        registry.addHandlerAdapter(new ControllerHandlerAdapter());

        assertThat(registry.getHandlerAdapter(controller)).isInstanceOf(ControllerHandlerAdapter.class);
    }

    @Test
    void HandlerExecution이면_RequestMappingHandlerAdapter를_반환한다() {
        registry.addHandlerAdapter(new ControllerHandlerAdapter());
        registry.addHandlerAdapter(new RequestMappingHandlerAdapter());

        assertThat(registry.getHandlerAdapter(handlerExecution)).isInstanceOf(RequestMappingHandlerAdapter.class);
    }

    @Test
    void 지원하는_어댑터가_여러_개면_먼저_등록된_어댑터를_반환한다() {
        final HandlerAdapter firstAdapter = new ControllerHandlerAdapter();
        final HandlerAdapter secondAdapter = new ControllerHandlerAdapter();
        registry.addHandlerAdapter(firstAdapter);
        registry.addHandlerAdapter(secondAdapter);

        assertThat(registry.getHandlerAdapter(controller)).isSameAs(firstAdapter);
    }

    @Test
    void 지원하는_어댑터가_없으면_예외가_발생한다() {
        registry.addHandlerAdapter(new ControllerHandlerAdapter());

        assertThatThrownBy(() -> registry.getHandlerAdapter(handlerExecution))
                .isInstanceOf(AdapterNotFoundException.class)
                .hasMessageContaining(HandlerExecution.class.getName());
    }

    @Test
    void 등록된_어댑터가_없으면_예외가_발생한다() {
        assertThatThrownBy(() -> registry.getHandlerAdapter(controller))
                .isInstanceOf(AdapterNotFoundException.class);
    }
}
