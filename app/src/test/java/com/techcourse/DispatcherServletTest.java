package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    private DispatcherServlet servlet;
    private ManualHandlerMapping manualHandlerMapping;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet();
        servlet.init();
        manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    @DisplayName("수동 매핑의 /register/view 요청은 회원가입 화면으로 이동한다")
    void manualMapping() throws Exception {
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("어노테이션 매핑의 GET /register 요청은 회원가입 화면으로 이동한다")
    void annotationGetMapping() throws Exception {
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        assertThat(manualHandlerMapping.getHandler(request)).isNull();

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("어노테이션 매핑의 POST /register 요청은 사용자를 저장하고 리다이렉트한다")
    void annotationPostMapping() throws Exception {
        final var account = UUID.randomUUID().toString();
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("test@example.com");

        assertThat(manualHandlerMapping.getHandler(request)).isNull();

        servlet.service(request, response);

        assertThat(InMemoryUserRepository.findByAccount(account))
                .hasValueSatisfying(user -> assertThat(user.checkPassword("password")).isTrue());
        verify(response).sendRedirect("/index.jsp");
    }
}
