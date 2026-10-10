package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import samples.TestController;

class AnnotationHandlerAdapterTest {

    private final AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();

    @Test
    void supports() throws Exception {
        assertThat(handlerAdapter.supports(createHandlerExecution())).isTrue();
        assertThat(handlerAdapter.supports(new Object())).isFalse();
    }

    @Test
    void handle() throws Exception {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");

        //when
        final var modelAndView = handlerAdapter.handle(request, response, createHandlerExecution());

        //then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    private HandlerExecution createHandlerExecution() throws NoSuchMethodException {
        final var method = TestController.class.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        return new HandlerExecution(new TestController(), method);
    }
}
