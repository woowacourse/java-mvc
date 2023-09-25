package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import webmvc.org.springframework.web.servlet.mvc.tobe.DispatcherServlet;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler_mapping.AnnotationHandlerMapping;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet(
            new AnnotationHandlerMapping(getClass().getPackage().getName())
        );
        dispatcherServlet.init();
    }

    @Nested
    @DisplayName("/register로 접근 시, ")
    class registerHandler {

        private final HttpServletRequest request = mock(HttpServletRequest.class);
        private final HttpServletResponse response = mock(HttpServletResponse.class);

        @BeforeEach
        void setUp() {
            when(request.getRequestURI()).thenReturn("/register");
        }

        @Test
        @DisplayName("/register에 GET 메서드로 접근 시 /register.jsp를 반환한다.")
        void show() throws ServletException, IOException {
            //given
            final ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
            final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
            when(request.getRequestURI()).thenReturn("/register/view");
            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestDispatcher(argumentCaptor.capture()))
                .thenReturn(requestDispatcher);

            //when
            dispatcherServlet.service(request, response);

            //then
            assertThat(argumentCaptor.getValue())
                .isEqualTo("/register.jsp");
        }

        @Test
        @DisplayName("/register에 POST 메서드로 접근 시 유저를 저장하고, /index.jsp로 리다이렉트한다..")
        void save() throws ServletException, IOException {
            //given
            final String account = "account";
            final String password = "password";
            final String email = "email";
            final long unvalidatedId = Long.MIN_VALUE;
            when(request.getMethod()).thenReturn("POST");
            when(request.getParameter("account")).thenReturn(account);
            when(request.getParameter("password")).thenReturn(password);
            when(request.getParameter("email")).thenReturn(email);

            //when
            dispatcherServlet.service(request, response);

            //then
            final ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
            verify(response, times(1)).sendRedirect(argumentCaptor.capture());

            final Optional<User> savedUser = InMemoryUserRepository.findByAccount(account);
            final Optional<User> expectedUser = Optional.of(
                new User(unvalidatedId, account, password, email));

            assertAll(
                () -> assertThat(argumentCaptor.getValue())
                    .isEqualTo("/index.jsp"),
                () -> assertThat(savedUser)
                    .usingRecursiveComparison()
                    .ignoringFields("value.id")
                    .isEqualTo(expectedUser)
            );
        }
    }

}
