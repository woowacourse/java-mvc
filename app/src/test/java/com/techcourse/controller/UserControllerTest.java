package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserControllerTest {

    @DisplayName("요청의 파라미터를 통해 사용자를 찾아 반환한다.")
    @Test
    void should_returnUser_when_requestShowUser() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("account")).thenReturn("ever");

        UserController controller = new UserController();

        // when
        ModelAndView modelAndView = controller.show(request, response);

        // then
        Object actual = modelAndView.getObject("user");
        User expected = InMemoryUserRepository.findByAccount("ever")
                .orElseThrow();
        assertThat(actual).isInstanceOf(User.class);
        assertThat(actual).isEqualTo(expected);
    }
}
