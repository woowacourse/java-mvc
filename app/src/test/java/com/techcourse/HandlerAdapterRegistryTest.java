package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.samples.AnnotationController;
import com.techcourse.controller.LoginController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerAdapterRegistryTest {

    HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

    @BeforeEach
    public void setUp() {
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new ManualHandlerAdapter());
    }

    @DisplayName("인터페이스 기반 핸들러에 해당하는 핸들러어댑터를 반환한다.")
    @Test
    void getControllerHandlerAdapter() {
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(new LoginController());

        assertThat(handlerAdapter).isInstanceOf(ManualHandlerAdapter.class);
    }

    @DisplayName("어노테이션 기반 핸들러에 해당하는 핸들러어댑터를 반환한다.")
    @Test
    void getHandlerExecutionHandlerAdapter() throws NoSuchMethodException {
        HandlerExecution handlerExecution = new HandlerExecution(AnnotationController.class, AnnotationController.class.getMethod("execute", HttpServletRequest.class, HttpServletResponse.class));
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handlerExecution);

        assertThat(handlerAdapter).isInstanceOf(AnnotationHandlerAdapter.class);
    }
}
