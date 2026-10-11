package com.techcourse;

import com.interface21.webmvc.servlet.view.JsonView;
import com.techcourse.controller.UserController;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private final UserController controller = new UserController();
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @Test
    @DisplayName("조회한 사용자를 모델에 담고 JsonView를 반환한다")
    void returnsUserWithJsonView() {
        final var expectedUser = InMemoryUserRepository.findByAccount("gugu").orElseThrow();
        when(request.getParameter("account")).thenReturn("gugu");

        final var modelAndView = controller.show(request, response);

        assertThat(modelAndView.getObject("user")).isSameAs(expectedUser);
        assertThat(modelAndView.getView()).isInstanceOf(JsonView.class);
    }

    @Test
    @DisplayName("요청의 account 파라미터에 해당하는 사용자를 조회한다")
    void looksUpRequestedAccount() {
        final var user = new User(2, "controller-test-user", "password", "user@example.com");
        InMemoryUserRepository.save(user);
        when(request.getParameter("account")).thenReturn(user.getAccount());

        final var modelAndView = controller.show(request, response);

        assertThat(modelAndView.getObject("user")).isSameAs(user);
    }
}
