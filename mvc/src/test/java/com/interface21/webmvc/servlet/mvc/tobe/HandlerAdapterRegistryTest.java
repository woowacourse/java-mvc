package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspViewTest;

import samples.TestManualHandlerAdapter;

public class HandlerAdapterRegistryTest {
    private HandlerAdapterRegistry handlerAdapterRegistry;

    @BeforeEach
    void setup() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();

        registerHandlerAdapter(new TestManualHandlerAdapter());
        registerHandlerAdapter(new AnnotationHandlerAdapter());
    }

    @DisplayName("ManualHandlerAdapter(Legacy MVC)가 핸들러에 맞는 어댑터를 찾아준다.")
    @Test
    void test_GetHandlerAdapter_When_Use_ManualHandlerAdapter() {
        // given
        final Object handler = (Controller) (req, res) -> "";

        // when
        final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

        // then
        assertThat(handlerAdapter).isInstanceOf(TestManualHandlerAdapter.class);
    }

    @DisplayName("AnnotationHandlerAdapter(@MVC)가 핸들러에 맞는 어댑터를 찾아준다.")
    @Test
    void test_GetHandlerAdapter_When_Use_AnnotationHandlerAdapter() {
        // given
        final Object handler = new HandlerExecution(null, null);

        // when
        final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

        // then
        assertThat(handlerAdapter).isInstanceOf(AnnotationHandlerAdapter.class);
    }

    @DisplayName("핸들러 처리에 적합한 핸들러 어댑터가 없으면, IllegalArgumentException이 발생한다.")
    @Test
    void test_GetHandler_ThrowError_When_NoHandlerAdapter() {
        // given
        final Object handler = new JspViewTest();

        // when & then
        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(handler))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("핸들러 처리에 적합한 핸들러 어댑터를 찾을 수 없습니다.");
    }

    private void registerHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }
}
