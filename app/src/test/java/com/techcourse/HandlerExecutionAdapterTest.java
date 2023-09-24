package com.techcourse;

import com.techcourse.controller.RegisterController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerExecutionAdapterTest {

    private HandlerExecutionAdapter handlerExecutionAdapter = new HandlerExecutionAdapter();

    @Test
    void supports() throws Exception {
        //given
        RegisterController registerController = RegisterController.class.newInstance();
        Method method= registerController.getClass()
                .getDeclaredMethod("viewRegister", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(registerController, method);

        //when
        boolean supports = handlerExecutionAdapter.supports(handlerExecution);

        //then
        assertThat(supports).isTrue();
    }

    @Test
    void adapt() throws Exception {
        //given
        RegisterController registerController = RegisterController.class.newInstance();
        Method method= registerController.getClass()
                .getDeclaredMethod("viewRegister", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(registerController, method);

        //when, then
        assertThat(handlerExecutionAdapter.adapt(null, null, handlerExecution))
                .isInstanceOf(ModelAndView.class);
    }
}
