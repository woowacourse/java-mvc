package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.legacy.Controller;

import samples.TestAnnotationController;
import samples.TestExtendsController;

class HandlerExecutionHandlerAdapterTest {

    @Nested
    @DisplayName("HandlerExecution 인 경우 지원 확인")
    class Supports {

        @Test
        @DisplayName("HandlerExecution 인 경우 지원 확인: 참")
        void supports() {
            final TestAnnotationController controllerInstance = new TestAnnotationController();
            final Method getMethod = TestAnnotationController.class.getDeclaredMethods()[0];
            final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, getMethod);

            final HandlerExecutionHandlerAdapter handlerExecutionHandlerAdapter = new HandlerExecutionHandlerAdapter();
            assertTrue(handlerExecutionHandlerAdapter.supports(handlerExecution));
        }

        @Test
        @DisplayName("HandlerExecution 인 경우 지원 확인: 실패")
        void supports_WhenHandlerIsNotHandlerExecution() {
            final Controller controller = new TestExtendsController();

            final HandlerExecutionHandlerAdapter handlerExecutionHandlerAdapter = new HandlerExecutionHandlerAdapter();
            assertFalse(handlerExecutionHandlerAdapter.supports(controller));
        }
    }

    @Nested
    @DisplayName("HandlerExecution 인 경우 핸들러 실행")
    class Handler {

        private HttpServletRequest request;
        private HttpServletResponse response;
        private Class<?> testController;

        @BeforeEach
        void setUp() throws ClassNotFoundException {
            request = mock(HttpServletRequest.class);
            response = mock(HttpServletResponse.class);

            testController = Class.forName("samples.TestAnnotationController");
        }

        @Test
        @DisplayName("HandlerExecution 인 경우 핸들러 실행 성공")
        void handle() throws NoSuchMethodException {
            // given
            when(request.getAttribute("id")).thenReturn("gugu");
            when(request.getRequestURI()).thenReturn("/get-test");
            when(request.getMethod()).thenReturn("GET");

            final HandlerExecution handlerExecution = new HandlerExecution(
                    new TestAnnotationController(),
                    testController.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class)
            );

            // when
            final HandlerExecutionHandlerAdapter handlerExecutionHandlerAdapter = new HandlerExecutionHandlerAdapter();

            // then
            final ModelAndView modelAndView = handlerExecutionHandlerAdapter.handle(handlerExecution, request, response);
            assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
        }
    }
}
