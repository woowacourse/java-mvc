package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;

public class LoginControllerTest extends ControllerTest {
    @Test
    void post() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession httpSession = mock(HttpSession.class);

        User user = new User(1L, "gugu", "password", "gugu@gugu");
        InMemoryUserRepository.save(user);

        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn(user.getAccount());
        when(request.getParameter("password")).thenReturn(user.getPassword());
        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(UserSession.SESSION_KEY)).thenReturn(null);

        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getView()).isEqualTo("redirect:/");
    }
}
