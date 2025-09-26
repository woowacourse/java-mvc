package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserControllerTest {

    private UserController userController;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        this.userController = new UserController();
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);

        // 테스트를 위한 사용자 데이터를 미리 저장
        InMemoryUserRepository.save(new User(2L, "gugu", "password", "gugu@email.com"));
    }

    @Test
    void 유저_조회() {
        // given
        final String account = "gugu";
        when(request.getParameter("account")).thenReturn(account);

        // when
        final ModelAndView modelAndView = userController.show(request, response);

        // then
        assertThat(modelAndView.getView()).isInstanceOf(JsonView.class);

        final Map<String, Object> model = modelAndView.getModel();
        final User user = (User) model.get("user");

        assertThat(user.getAccount()).isEqualTo(account);
    }
}
