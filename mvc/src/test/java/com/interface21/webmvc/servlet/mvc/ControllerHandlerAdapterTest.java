package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;

import samples.TestAnnotationController;
import samples.TestExtendsController;

class ControllerHandlerAdapterTest {

    @Nested
    class Supports {

        @Test
        @DisplayName("Controller 인 경우 지원 확인: 참")
        void supports() {
            final Controller controller = new TestExtendsController();

            final ControllerHandlerAdapter controllerHandlerAdapter = ControllerHandlerAdapter.getInstance();
            assertTrue(controllerHandlerAdapter.supports(controller));
        }

        @Test
        @DisplayName("HandlerExecution 인 경우 지원 확인: 실패")
        void supports_WhenHandlerIsNotHandlerExecution() {
            final TestAnnotationController controllerInstance = new TestAnnotationController();
            final Method getMethod = TestAnnotationController.class.getDeclaredMethods()[0];
            final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, getMethod);

            final ControllerHandlerAdapter controllerHandlerAdapter = ControllerHandlerAdapter.getInstance();
            assertFalse(controllerHandlerAdapter.supports(handlerExecution));
        }
    }

    @Nested
    @DisplayName("Controller 핸들러 실행")
    class Handler {

        @Test
        @DisplayName("Controller 핸들러 실행 성공: ModelAndView 반환")
        void handle() {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);
            final Controller testController = new TestExtendsController();

            // when
            final ControllerHandlerAdapter controllerHandlerAdapter = ControllerHandlerAdapter.getInstance();

            // then
            final ModelAndView modelAndView = controllerHandlerAdapter.handle(testController, request, response);
            assertAll(
                    () -> assertThat(modelAndView.getView()).isEqualTo(new JspView("/test.jsp")),
                    () -> assertThat(modelAndView.getModel()).isEmpty()
            );
        }
    }
}
