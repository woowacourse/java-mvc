package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @Test
    void annotation_MVC_기반_회원가입_뷰_페이지_반환() throws ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher(any())).thenReturn(mock());

        ArgumentCaptor<String> viewNameCaptor = ArgumentCaptor.forClass(String.class);

        dispatcherServlet.service(request, response);

        verify(request).getRequestDispatcher(viewNameCaptor.capture());
        String actual = viewNameCaptor.getValue();

        assertThat(actual).isEqualTo("/register.jsp");
    }

    @Test
    void legacy_MVC_기반_로그인_뷰_페이지_반환() throws ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getSession()).thenReturn(mock());
        when(request.getRequestDispatcher(any())).thenReturn(mock());

        ArgumentCaptor<String> viewNameCaptor = ArgumentCaptor.forClass(String.class);

        dispatcherServlet.service(request, response);

        verify(request).getRequestDispatcher(viewNameCaptor.capture());
        String actual = viewNameCaptor.getValue();

        assertThat(actual).isEqualTo("/login.jsp");
    }
}
