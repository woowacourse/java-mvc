package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestMethod;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HandlerAdaptorRegistryTest {

    private HandlerAdaptorRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new HandlerAdaptorRegistry();
    }

    @Test
    @DisplayName("Handler 에 맞는 적절한 HandlerAdaptor 를 찾아 반환한다.")
    void test() throws NoSuchMethodException {
        Class<?> clazz = TestMethod.class;
        Method method = clazz.getDeclaredMethod("test", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handler = new HandlerExecution(clazz, method);

        registry.addHandlerAdaptor(new HandlerExecutionAdaptor());
        ;

        HandlerAdaptor handlerAdaptor = registry.getHandlerAdaptor(handler);

        assertThat(handlerAdaptor).isInstanceOf(HandlerExecutionAdaptor.class);
    }

    @Test
    @DisplayName("Handler 에 맞는 적절한 HandlerAdaptor 가 없는 경우 예외를 발생한다.")
    void test_exception() throws NoSuchMethodException {
        Class<?> clazz = TestMethod.class;
        Method method = clazz.getDeclaredMethod("test", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handler = new HandlerExecution(clazz, method);

        assertThatThrownBy(() -> registry.getHandlerAdaptor(handler))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Can not find proper adaptor from handler:");
    }
}
