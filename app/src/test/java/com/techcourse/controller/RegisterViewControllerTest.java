package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;

class RegisterViewControllerTest {

    @Test
    @DisplayName("@Controller를 사용하여 핸들러를 등록한다.")
    void getController() throws NoSuchMethodException {
        //given
        final HandlerMapping handlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        handlerMapping.initialize();
        final HandlerExecution expected = new HandlerExecution(new RegisterViewController(),
                RegisterViewController.class.getMethod("getView", HttpServletRequest.class, HttpServletResponse.class));

        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getMethod()).thenReturn(RequestMethod.GET.name());

        //when && then
        assertThat(handlerMapping.getHandler(request)).isEqualTo(expected);
    }
}
