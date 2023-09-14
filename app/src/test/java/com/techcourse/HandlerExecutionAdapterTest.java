package com.techcourse;

import com.techcourse.controller.mvc.MvcRegisterController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerExecutionAdapterTest {

    private HandlerExecutionAdapter handlerExecutionAdapter = new HandlerExecutionAdapter();

    @Test
    void supports() throws Exception {
        //given
        MvcRegisterController mvcRegisterController = MvcRegisterController.class.newInstance();
        Method method= mvcRegisterController.getClass()
                .getDeclaredMethod("viewRegister", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(mvcRegisterController, method);

        //when
        boolean supports = handlerExecutionAdapter.supports(handlerExecution);

        //then
        assertThat(supports).isTrue();
    }

    @Test
    void adapt() throws Exception {
        //given
        MvcRegisterController mvcRegisterController = MvcRegisterController.class.newInstance();
        Method method= mvcRegisterController.getClass()
                .getDeclaredMethod("viewRegister", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(mvcRegisterController, method);

        //when
        String viewName = handlerExecutionAdapter.adapt(null, null, handlerExecution);

        //then
        assertThat(viewName).isEqualTo("/register.jsp");
    }
}