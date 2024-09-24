package com.interface21.webmvc.servlet.mvc;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;

import samples.TestController;

class HandlerExecutionHandlerAdapterTest {

    @Nested
    @DisplayName("HandlerExecution 인 경우 지원 확인")
    class Supports {

        @Test
        @DisplayName("HandlerExecution 인 경우 지원 확인: 참")
        void supports() {
            final TestController controllerInstance = new TestController();
            final Method getMethod = TestController.class.getDeclaredMethods()[0];
            final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, getMethod);

            final HandlerExecutionHandlerAdapter handlerExecutionHandlerAdapter = new HandlerExecutionHandlerAdapter();
            assertTrue(handlerExecutionHandlerAdapter.supports(handlerExecution));
        }

        @Test
        @DisplayName("HandlerExecution 인 경우 지원 확인: 실패")
        void supports_WhenHandlerIsNotHandlerExecution() {
            final Controller controller = new SupportController();

            final HandlerExecutionHandlerAdapter handlerExecutionHandlerAdapter = new HandlerExecutionHandlerAdapter();
            assertFalse(handlerExecutionHandlerAdapter.supports(controller));
        }

        private class SupportController implements Controller {
            @Override
            public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
                return "";
            }
        }
    }

    @Nested
    class Handler {

        @Test
        void handle() {

        }

    }
}
