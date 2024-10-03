package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.JsonView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserControllerTest {

    private UserController userController;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        userController = new UserController();

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        InMemoryUserRepository.save(new User(1L, "jazz", "password", "jazz@woowa.com"));
    }

    @DisplayName("정상적으로 User 정보를 불러온다.")
    @Test
    void successShow() {
        when(request.getParameter("account")).thenReturn("jazz");

        ModelAndView modelAndView = userController.show(request, response);

        View view = modelAndView.getView();
        Map<String, Object> model = modelAndView.getModel();
        User user = (User) model.get("user");

        assertAll(
                () -> assertThat(user.getAccount()).isEqualTo("jazz"),
                () -> assertThat(view).isInstanceOf(JsonView.class)
        );
    }

    @DisplayName("존재하지 않으면 Account라면 예외를 반환한다.")
    @Test
    void throwsWhenNotFoundAccount() {
        when(request.getParameter("account")).thenReturn("kargo");

        assertThatThrownBy(() -> userController.show(request, response))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
