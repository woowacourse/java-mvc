package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.HandlerMappingRegistry;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private DispatcherServlet servlet;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new DispatcherServlet(new HandlerMappingRegistry(), new HandlerAdapterRegistry());
        servlet.init();
    }

    @Test
    @DisplayName("어노테이션 컨트롤러로 회원을 등록하고 결과 화면으로 이동한다")
    void registeringUserRedirectsAndSavesUser() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String account = "registration-" + System.nanoTime();
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn("secret");
        when(request.getParameter("email")).thenReturn("user@example.com");

        servlet.service(request, response);

        verify(response).sendRedirect("/index.jsp");
        assertThat(InMemoryUserRepository.findByAccount(account)).isPresent();
    }

    @Test
    @DisplayName("레거시 컨트롤러로 메인 화면을 포워드한다")
    void legacyControllerForwardsToIndex() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }
}
