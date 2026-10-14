package com.techcourse.controller;

import com.interface21.webmvc.servlet.view.JsonView;
import com.techcourse.controller.dto.ErrorResponse;
import com.techcourse.controller.dto.UserResponse;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Test
    void returnsExplicitUserResponse() {
        final String account = UUID.randomUUID().toString();
        InMemoryUserRepository.save(new User(3, account, "secret", "user@example.com"));
        final var request = mock(HttpServletRequest.class);
        when(request.getParameter("account")).thenReturn(account);

        final var mav = new UserController().show(request, mock(HttpServletResponse.class));

        assertThat(mav.getView()).isInstanceOf(JsonView.class);
        assertThat(mav.getModel()).containsOnlyKeys("user");
        assertThat(mav.getObject("user")).isEqualTo(new UserResponse(account));
    }

    @Test
    void rejectsMissingEmptyAndBlankAccounts() {
        for (final String account : new String[]{null, "", " ", "\t "}) {
            final var request = mock(HttpServletRequest.class);
            final var response = mock(HttpServletResponse.class);
            when(request.getParameter("account")).thenReturn(account);

            final var mav = new UserController().show(request, response);

            verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
            assertThat(mav.getView()).isInstanceOf(JsonView.class);
            assertThat(mav.getModel()).containsOnlyKeys("error");
            assertThat(mav.getObject("error"))
                    .isEqualTo(new ErrorResponse("INVALID_ACCOUNT", "account is required"));
        }
    }

    @Test
    void returnsNotFoundForUnknownAccount() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getParameter("account")).thenReturn(UUID.randomUUID().toString());

        final var mav = new UserController().show(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        assertThat(mav.getObject("error"))
                .isEqualTo(new ErrorResponse("USER_NOT_FOUND", "user not found"));
    }
}
