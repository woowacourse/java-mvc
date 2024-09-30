package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import samples.TestController;

class AnnotationHandlerAdapterTest {

    @Test
    @DisplayName("어노테이션 핸들러 어댑터에 메뉴얼 핸들러가 들어오면 예외가 발생한다.")
    void throw_exception_when_different_type_handler() {
        // given
        final var annotationHandlerAdapter = new AnnotationHandlerAdapter();
        final Object controller = new TestController();

        // when & then
        assertThrows(IllegalArgumentException.class, () -> annotationHandlerAdapter.handle(controller, null, null));
    }

    @Test
    @DisplayName("어노테이션 핸들러 어댑터에 어노테이션 핸들러가 들어오면 핸들러를 실행한다.")
    void execute_handler_when_same_type_handler() throws Exception {
        // given
        final var annotationHandlerAdapter = new AnnotationHandlerAdapter();
        final Method method = TestController.class.getMethod("save", HttpServletRequest.class,
                HttpServletResponse.class);
        final Object handlerExecution = new HandlerExecution(new TestController(), method);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("lemon");
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/post-test");

        // when
        final var modelAndView = annotationHandlerAdapter.handle(handlerExecution, request, response);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("lemon");
    }
}
