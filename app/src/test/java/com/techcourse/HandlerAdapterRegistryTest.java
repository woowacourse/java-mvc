package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.techcourse.controller.LoginController;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry handlerAdapterRegistry;

    @BeforeEach
    void setUp() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new ManualHandlerAdapter());
    }

    @DisplayName("ManualHandlerAdapter를 찾을 수 있다.")
    @Test
    void getHandlerAdapter_Manual() throws Exception {
        assertThat(handlerAdapterRegistry.getHandlerAdapter(new LoginController()))
                .isExactlyInstanceOf(ManualHandlerAdapter.class);
    }

    @DisplayName("AnnotationHandlerAdapter를 찾을 수 있다.")
    @Test
    void getHandlerAdapter_Annotation() throws Exception {
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        assertThat(handlerAdapterRegistry.getHandlerAdapter(handlerExecution))
                .isExactlyInstanceOf(AnnotationHandlerAdapter.class);
    }

    @DisplayName("HandlerAdapter를 찾지 못한 경우 예외 발생")
    @Test
    void getHandlerAdapter_Fail() {
        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(String.class))
                .isInstanceOf(ServletException.class)
                .hasMessage("해당 handler를 지원하는 HandlerAdapter를 찾지 못했습니다.");
    }
}
