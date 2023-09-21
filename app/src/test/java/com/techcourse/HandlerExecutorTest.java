package com.techcourse;

import com.techcourse.controller.legacy.RegisterViewController;
import com.techcourse.controller.mvc.MvcRegisterController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HandlerExecutorTest {

    @Test
    void executeIfControllerHandler() throws Exception {
        //given
        HandlerExecutor handlerExecutor = new HandlerExecutor();
        Controller controller = RegisterViewController.class.newInstance();

        //when
        String viewName = handlerExecutor.execute(null, null, controller);

        //then
        assertEquals("/register.jsp", viewName);
    }

    @Test
    void executeIfHandlerExecution() throws Exception {
        //given
        HandlerExecutor handlerExecutor = new HandlerExecutor();

        MvcRegisterController mvcRegisterController = MvcRegisterController.class.newInstance();
        Method method= mvcRegisterController.getClass()
                .getDeclaredMethod("viewRegister", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(mvcRegisterController, method);

        //when
        String viewName = handlerExecutor.execute(null, null, handlerExecution);

        //then
        assertEquals("/register.jsp", viewName);
    }

    @Test
    void executeIfNoneHandler() throws Exception {
        //given
        HandlerExecutor handlerExecutor = new HandlerExecutor();

        //when
        String viewName = handlerExecutor.execute(null, null, null);

        //then
        assertEquals("404.jsp", viewName);
    }
}