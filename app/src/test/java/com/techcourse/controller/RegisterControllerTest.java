package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegisterControllerTest {

    @DisplayName("/register 경로로 접근하면 register.jsp 를 반환한다.")
    @Test
    void show() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RegisterController controller = new RegisterController();

        ModelAndView modelAndView = controller.show(request, response);

        assertThat(modelAndView).isEqualTo(new ModelAndView(new JspView("register.jsp")));
    }

    @DisplayName("/register 경로로 POST 요청을 보내면 회원을 저장하고 redirect:/ 를 반환한다.")
    @Test
    void save() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        given(request.getParameter("account")).willReturn("test");
        given(request.getParameter("password")).willReturn("1234");
        given(request.getParameter("email")).willReturn("test@test.com");

        RegisterController controller = new RegisterController();

        ModelAndView modelAndView = controller.save(request, response);

        assertThat(InMemoryUserRepository.findByAccount("test")).isPresent();
        assertThat(modelAndView).isEqualTo(new ModelAndView(new JspView("redirect:/")));
    }
}
