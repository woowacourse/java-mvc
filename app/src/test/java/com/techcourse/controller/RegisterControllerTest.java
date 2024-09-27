package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegisterControllerTest {

    @DisplayName("Controller 인터페이스 구현 메서드 execute 실행시 viewName을 반환한다.")
    @Test
    void execute() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        given(request.getParameter("account")).willReturn("test");
        given(request.getParameter("password")).willReturn("1234");
        given(request.getParameter("email")).willReturn("test@test.com");

        RegisterController controller = new RegisterController();

        String result = controller.execute(request, response);

        assertThat(result).isEqualTo("redirect:/index.jsp");
    }

    @DisplayName("@RequestMapping 메서드 save 실행시 ModelAndView 를 반환한다.")
    @Test
    void save() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        given(request.getParameter("account")).willReturn("test");
        given(request.getParameter("password")).willReturn("1234");
        given(request.getParameter("email")).willReturn("test@test.com");

        RegisterController controller = new RegisterController();

        ModelAndView modelAndView = controller.save(request, response);

        assertThat(modelAndView).isNotNull();
        assertThat(modelAndView instanceof ModelAndView).isTrue();
    }
}
