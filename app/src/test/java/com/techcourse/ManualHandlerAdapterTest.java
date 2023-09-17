package com.techcourse;

import com.techcourse.mvc.ManualHandlerAdapter;
import com.techcourse.controller.RegisterController;
import com.techcourse.controller.RegisterViewController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.view.JspView;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

class ManualHandlerAdapterTest {

    private ManualHandlerAdapter manualHandlerAdapter;

    @BeforeEach
    void setUp() {
        manualHandlerAdapter = new ManualHandlerAdapter();
    }

    @Test
    void handle() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final var registerController = new RegisterViewController();
        final var method = registerController.getClass().getMethod("execute", HttpServletRequest.class, HttpServletResponse.class);
        final var handlerExecution = new HandlerExecution(registerController, method);

        // when
        final ModelAndView result = manualHandlerAdapter.handle(request, response, handlerExecution);

        // then
        assertThat(result.getView())
                .usingRecursiveComparison()
                .isEqualTo(new JspView("/register.jsp"));
    }

    @Test
    void supports() throws Exception {
        // given
        final var registerController = new RegisterController();
        final var method = registerController.getClass().getMethod("execute", HttpServletRequest.class, HttpServletResponse.class);
        final var handlerExecution = new HandlerExecution(registerController, method);

        // when
        final boolean supports = manualHandlerAdapter.supports(handlerExecution);

        // then
        assertThat(supports).isTrue();
    }
}
