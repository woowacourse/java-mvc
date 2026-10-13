package com.techcourse.controller;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JsonView;
import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Test
    void mapsApiUserToTheAnnotationControllerAndReturnsItsUserModel() throws Exception {
        final var handlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        handlerMapping.initialize();

        final var request = mock(HttpServletRequest.class);
        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn("gugu");

        final var handler = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handler.handle(request, mock(HttpServletResponse.class));

        assertThat(modelAndView.getView()).isInstanceOf(JsonView.class);
        assertThat(((User) modelAndView.getObject("user")).getAccount()).isEqualTo("gugu");
    }
}
