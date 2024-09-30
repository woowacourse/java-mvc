package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    void annotation_MVC_기반_로그인_뷰_페이지_반환() throws ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(mock());
        when(request.getRequestDispatcher(any())).thenReturn(mock());

        ArgumentCaptor<String> viewNameCaptor = ArgumentCaptor.forClass(String.class);

        dispatcherServlet.service(request, response);

        verify(request).getRequestDispatcher(viewNameCaptor.capture());
        String actual = viewNameCaptor.getValue();

        assertThat(actual).isEqualTo("/login.jsp");
    }

    @Test
    void 매핑되는_URI가_없으면_예외_발생() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/invalid-uri");
        when(request.getMethod()).thenReturn("GET");

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("지원하지 않는 Handler 입니다.");
    }

    @Test
    void request_URI가_null이면_예외_발생() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn(null);

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("request URI는 null일 수 없습니다.");
    }

    @Test
    void 지원하는_method가_없으면_예외_발생() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("invalid-method");

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("지원하지 않는 request method 입니다.");
    }

    @Test
    void request_method가_null이면_예외_발생() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn(null);

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("request URI와 http method는 null일 수 없습니다.");
    }
}
