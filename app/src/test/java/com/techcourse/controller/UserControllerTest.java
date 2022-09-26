package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import com.techcourse.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;

class UserControllerTest {

    private final UserRepository userRepository = new InMemoryUserRepository();
    private final UserController userController = new UserController(userRepository);

    @Test
    void 유저_정보를_JSON으로_반환한다() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("account")).thenReturn("dummy");

        User user = new User(0, "dummy", "password", "dummy@woowahan.com");
        userRepository.save(user);

        ModelAndView modelAndView = userController.show(request, response);

        assertThat(modelAndView.getModel()).containsAllEntriesOf(Map.of("user", user));
    }

    @Test
    void 없는_유저_정보를_조회하려_하면_404_jsp로_redirect_한다() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("account")).thenReturn("dummy");

        ModelAndView modelAndView = userController.show(request, response);

        assertThat(modelAndView.getView()).usingRecursiveComparison()
                .isEqualTo(new JspView("redirect:/404.jsp"));
    }
}
