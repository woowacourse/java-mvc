package com.interface21.web.servlet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.web.servlet.mvc.ControllerHandlerAdapter;
import com.interface21.web.servlet.mvc.HandlerExecutionAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HandlerAdapterTest {

    @DisplayName("ControllerHandlerAdapter 테스트")
    @Nested
    class ControllerHandlerAdapterTest {

        static class InterfaceControllerHandler implements Controller {

            @Override
            public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
                return "index.jsp";
            }
        }

        @DisplayName("컨트롤러 인터페이스 기반 mvc supports 메서드 테스트")
        @Test
        void supports() {
            HandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();
            assertTrue(controllerHandlerAdapter.supports(new InterfaceControllerHandler()));
        }

        @DisplayName("컨트롤러 인터페이스 기반 mvc handle 메서드 테스트")
        @Test
        void handle() throws Exception {
            HandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpServletResponse response = mock(HttpServletResponse.class);

            ModelAndView actual = controllerHandlerAdapter.handle(request, response, new InterfaceControllerHandler());

            Field viewNameField = actual.getView().getClass().getDeclaredField("viewName");
            viewNameField.setAccessible(true);
            Object viewName = viewNameField.get(actual.getView());
            assertEquals("index.jsp", viewName);
        }
    }

    @DisplayName("HandlerExecutionAdapter 테스트")
    @Nested
    class HandlerExecutionAdapterTest {

        HandlerExecutionAdapterTest() throws NoSuchMethodException {
        }

        @com.interface21.context.stereotype.Controller
        static class AnnotationController {

            @RequestMapping(value = "/test", method = RequestMethod.GET)
            public ModelAndView findIndex(HttpServletRequest request, HttpServletResponse response) {
                return new ModelAndView(new JspView("index.jsp"));
            }
        }

        private final Method findIndex = AnnotationController.class.getMethod("findIndex", HttpServletRequest.class,
                HttpServletResponse.class);
        private final HandlerExecution handlerExecution = new HandlerExecution(
                new AnnotationController(),
                findIndex
        );

        @DisplayName("HandlerExecution 인터페이스 기반 mvc supports 메서드 테스트")
        @Test
        void supports() {
            HandlerAdapter handlerExecutionAdapter = new HandlerExecutionAdapter();
            assertTrue(handlerExecutionAdapter.supports(handlerExecution));
        }

        @DisplayName("HandlerExecution 인터페이스 기반 mvc handle 메서드 테스트")
        @Test
        void handle() throws Exception {
            HandlerAdapter handlerExecutionAdapter = new HandlerExecutionAdapter();
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpServletResponse response = mock(HttpServletResponse.class);
            findIndex.setAccessible(true);

            ModelAndView actual = handlerExecutionAdapter.handle(request, response, handlerExecution);

            Field viewNameField = actual.getView().getClass().getDeclaredField("viewName");
            viewNameField.setAccessible(true);
            Object viewName = viewNameField.get(actual.getView());
            assertEquals("index.jsp", viewName);
        }
    }
}
