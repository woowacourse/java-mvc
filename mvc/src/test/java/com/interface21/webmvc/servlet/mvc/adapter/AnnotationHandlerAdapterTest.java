package com.interface21.webmvc.servlet.mvc.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerExecution;
import com.techcourse.controller.LoginController;

class AnnotationHandlerAdapterTest {

    @Nested
    @DisplayName("지원하는 핸들러인지 판단")
    class Supports {

        @Test
        @DisplayName("성공 : 지원하는 핸들러(HandlerExecution)는 true 반환")
        void supportsTrue() {
            AnnotationHandlerAdapter adapter = new AnnotationHandlerAdapter();
            LoginController instance = new LoginController();

            boolean actual = adapter.supports(new HandlerExecution(instance, instance.getClass().getEnclosingMethod()));

            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("성공 : 지원하는 핸들러는 false 반환")
        void supportsFalse() {
            AnnotationHandlerAdapter adapter = new AnnotationHandlerAdapter();

            boolean actual = adapter.supports("not exists");

            assertThat(actual).isFalse();
        }
    }

    @Test
    @DisplayName("핸들러 실행")
    void handle() {
        AnnotationHandlerAdapter adapter = new AnnotationHandlerAdapter();
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        ModelAndView actual = adapter.handle(request, response, handlerExecution);

        verify(handlerExecution).handle(request, response);
        assertThat(actual).isEqualTo(handlerExecution.handle(request, response));
    }
}
