package com.interface21.webmvc.servlet.mvc.tobe.handler.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.webmvc.servlet.mvc.tobe.handler.HandlerExecution;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.ExampleController;
import samples.TestController;

class HandlerAdaptersTest {

    private final HandlerExecution handlerExecution = createHandlerExecution();

    private HandlerExecution createHandlerExecution() {
        try {
            ExampleController exampleController = new ExampleController();
            Method method = exampleController.getClass()
                    .getMethod("method1", HttpServletRequest.class, HttpServletResponse.class);
            return new HandlerExecution(exampleController, method);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not create HandlerExecution");
        }
    }

    @DisplayName("핸들러에 맞는 어댑터가 없는 경우 예외가 발생한다.")
    @Test
    void noSuitableAdapter() {
        HandlerAdapters handlerAdapters = new HandlerAdapters();
        assertThatThrownBy(() -> handlerAdapters.getHandlerAdapter(new TestController()))
                .isInstanceOf(ServletException.class)
                .hasMessage("No suitable adapter found for handler");
    }

    @DisplayName("핸들러에 맞는 어댑터를 반환한다.")
    @Test
    void getHandlerAdapter() throws Exception {
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        HandlerAdapters handlerAdapters = new HandlerAdapters(annotationHandlerAdapter);

        assertThat(handlerAdapters.getHandlerAdapter(handlerExecution))
                .isEqualTo(annotationHandlerAdapter);
    }
}
