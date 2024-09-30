package com.interface21.handler;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class DispatcherServletTest {

    @Test
    @DisplayName("핸들러를 디스패치한다.")
    void dispatch_handler() throws ServletException {
        // given

        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        final HttpServletRequest request = new MockHttpServletRequest("GET", "/");
        final HttpServletResponse response = new MockHttpServletResponse();
        dispatcherServlet.init();

        // when & then
        assertThatCode(() -> dispatcherServlet.service(request, response))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("핸들러가 없으면  디스패치 중 예외를 던진다.")
    void throw_exception_when_does_not_match_any_handler() {
        // given
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        final HttpServletRequest request = new MockHttpServletRequest("GET", "/never-ever");
        final HttpServletResponse response = new MockHttpServletResponse();
        dispatcherServlet.init();

        // when & then
        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class);
    }
}
