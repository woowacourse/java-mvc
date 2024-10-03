package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.legacy.Controller;
import com.interface21.webmvc.servlet.mvc.legacy.ControllerHandlerAdapter;

import samples.TestAnnotationController;
import samples.TestExtendsController;

class ControllerHandlerAdapterTest {

    @Nested
    class Supports {

        @Test
        @DisplayName("Controller 인 경우 지원 확인: 참")
        void supports() {
            final Controller controller = new TestExtendsController();

            final ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();
            assertTrue(controllerHandlerAdapter.supports(controller));
        }

        @Test
        @DisplayName("HandlerExecution 인 경우 지원 확인: 실패")
        void supports_WhenHandlerIsNotHandlerExecution() {
            final TestAnnotationController controllerInstance = new TestAnnotationController();
            final Method getMethod = TestAnnotationController.class.getDeclaredMethods()[0];
            final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, getMethod);

            final ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();
            assertFalse(controllerHandlerAdapter.supports(handlerExecution));
        }
    }

    @Nested
    @DisplayName("Controller 핸들러 실행")
    class Handle {

        @Test
        @DisplayName("Controller 핸들러 실행 성공: ModelAndView 반환")
        void handle() throws Exception {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);
            final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
            final ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

            when(request.getRequestDispatcher(argumentCaptor.capture())).thenReturn(requestDispatcher);

            // when
            final ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();

            // then
            final ModelAndView modelAndView = controllerHandlerAdapter.handle(new TestExtendsController(), request, response);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
            assertAll(
                    () -> assertThat(argumentCaptor.getValue()).isEqualTo("/test.jsp"),
                    () -> assertThat(modelAndView.getModel()).isEmpty()
            );
        }
    }
}
