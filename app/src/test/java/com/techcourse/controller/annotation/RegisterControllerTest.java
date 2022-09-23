package com.techcourse.controller.annotation;

import static nextstep.mvc.view.JspView.REDIRECT_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegisterControllerTest {

    private HandlerMapping handlerMapping = new AnnotationHandlerMapping("com.techcourse");

    @BeforeEach
    void setUp() {
        handlerMapping.initialize();
    }

    @Test
    @DisplayName("회원가입")
    void execute() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("josh");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("asd@gmail.com");

        HandlerExecution handler = (HandlerExecution) handlerMapping.getHandler(request);
        ModelAndView modelAndView = handler.handle(request, response);

        Class<? extends View> aClass = modelAndView.getView().getClass();
        Field field = aClass.getDeclaredField("viewName");
        field.setAccessible(true);

        assertThat(field.get(modelAndView.getView())).isEqualTo(REDIRECT_PREFIX + "/index.jsp");
    }
}
