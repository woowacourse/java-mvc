package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.interface21.webmvc.servlet.mvc.AnnotationHandlerAdapter;
import samples.AnnotationTestController;
import samples.TestController;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class AnnotationHandlerAdapterTest {

    private AnnotationHandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        handlerAdapter = new AnnotationHandlerAdapter();
    }

    @DisplayName("AnnotationHandler를 지원한다.")
    @Test
    void supports() {
        // given
        AnnotationTestController controller = new AnnotationTestController();
        Object handler = new HandlerExecution(controller.getClass().getDeclaredMethods()[0], controller);

        // when
        boolean result = handlerAdapter.supports(handler);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("AnnotationHandler외의 handler는 지원하지 않는다.")
    @Test
    void cannotSupport() {
        // given
        TestController controller = new TestController();
        Object handler = controller.getClass().getDeclaredMethods()[0];

        // when
        boolean result = handlerAdapter.supports(handler);

        // then
        assertThat(result).isFalse();
    }
}
