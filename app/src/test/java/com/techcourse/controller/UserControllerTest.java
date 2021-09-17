package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.support.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession session;
    UserController userController;

    @BeforeEach
    void setUp() {
        InMemoryUserRepository.removeAll();
        InMemoryUserRepository.save(new User(1L,"gugu", "password", "hkkang@woowahan.com"));

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);

        userController = new UserController();
    }

    @DisplayName("존재하는 유저의 정보를 조회하면 유저 정보를 담은 Json 응답이 생성된다.")
    @Test
    void showExistUser() {
        // given
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn(RequestMethod.GET.name());
        when(request.getParameter("account")).thenReturn("gugu");

        // when
        ModelAndView modelAndView = userController.show(request, response);
        Map<String, Object> model = modelAndView.getModel();
        User user = (User) model.get("user");

        // then
        assertThat(modelAndView.getView()).isInstanceOf(JsonView.class);
        assertThat(user.getAccount()).isEqualTo("gugu");
    }

    @DisplayName("존재하지 않는 유저의 정보를 조회하면 404.jsp로 이동한다.")
    @Test
    void showNonExistUser() {
        // given
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn(RequestMethod.GET.name());
        when(request.getParameter("account")).thenReturn("charlie");

        // when
        ModelAndView modelAndView = userController.show(request, response);

        // then
        assertThat(modelAndView.getViewName()).isEqualTo("404.jsp");
    }
}