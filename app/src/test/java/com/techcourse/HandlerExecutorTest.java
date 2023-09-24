package com.techcourse;

import com.techcourse.controller.RegisterController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HandlerExecutorTest {

    @Test
    void executeIfHandlerExecution() throws Exception {
        //given
        HandlerExecutor handlerExecutor = new HandlerExecutor();

        RegisterController registerController = RegisterController.class.newInstance();
        Method method= registerController.getClass()
                .getDeclaredMethod("viewRegister", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(registerController, method);

        //when, then
        assertThat(handlerExecutor.execute(null, null, handlerExecution))
                .isInstanceOf(ModelAndView.class);
    }

    @Test
    void executeIfNoneHandler() {
        //given
        HandlerExecutor handlerExecutor = new HandlerExecutor();

        //when, then
        assertThatThrownBy(() -> handlerExecutor.execute(null, null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
