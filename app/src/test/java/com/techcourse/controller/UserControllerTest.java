package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserControllerTest {

    @DisplayName("/api/user 경로로 GET 요청시 model 에 user 정보를 담아서 JsonView 를 반환한다.")
    @Test
    void show() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        given(request.getParameter("account")).willReturn("gugu");
        UserController userController = new UserController();

        ModelAndView modelAndView = userController.show(request, response);

        ModelAndView expected = new ModelAndView(new JsonView());
        expected.addObject("user", InMemoryUserRepository.findByAccount("gugu").get());
        
        assertThat(modelAndView.getModel()).isEqualTo(expected.getModel());
    }
}
