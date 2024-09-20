package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.tobe.handler.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.handler.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.handler.mapping.HandlerMappings;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerMappingsTest {

    private static final HandlerMappings handlerMappings = new HandlerMappings(
            new AnnotationHandlerMapping("samples")
    );

    @BeforeAll
    static void initializedHandlerMappings() {
        handlerMappings.initialize();
    }

    @DisplayName("요청을 처리할 수 있는 핸들러를 반환한다.")
    @Test
    void getHandler() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        HandlerExecution handlerExecution = (HandlerExecution) handlerMappings.getHandler(request);

        HandlerExecution expectedHandlerExecution = new HandlerExecution(
                new TestController(),
                TestController.class.getMethod("save", HttpServletRequest.class, HttpServletResponse.class)
        );
        assertThat(handlerExecution).isEqualTo(expectedHandlerExecution);
    }

    @DisplayName("요청을 처리할 수 있는 핸들러가 없는 경우 예외가 발생한다.")
    @Test
    void noHandlerFound() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/invalid-uri");
        when(request.getMethod()).thenReturn("POST");

        assertThatThrownBy(() -> handlerMappings.getHandler(request))
                .isInstanceOf(ServletException.class)
                .hasMessage("No handler found for request");
    }
}
