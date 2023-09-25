package com.techcourse.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RegisterControllerTest {

    @Test
    void 회원가입_테스트() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getParameter("account")).thenReturn("power");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("power@naver.com");
        when(request.getSession()).thenReturn(session);

        RegisterController registerController = new RegisterController();
        ModelAndView register = registerController.register(request, response);

        Assertions.assertThat(register.getView()).isEqualTo(JspView.redirect("/index.jsp"));
        User user = InMemoryUserRepository.findByAccount("power").get();
        Assertions.assertThat(user.checkPassword("password")).isTrue();
    }
}
