package com.techcourse;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.ApplicationContext;
import webmvc.org.springframework.web.servlet.mvc.DispatcherServlet;

class DispatcherServletTest {

    @DisplayName("해당하는 HandlerMapping 이 존재하지 않으면 예외를 반환한다.")
    @Test
    void notExistHandlerMappingThrowsException() {
        // given
        final DispatcherServlet dispatcherServlet = new DispatcherServlet(
            new ApplicationContext("com.techcourse"));
        dispatcherServlet.init();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/not-exist");
        when(request.getMethod()).thenReturn("GET");

        // when
        // then
        assertThatThrownBy(() -> dispatcherServlet.service(request, mock(HttpServletResponse.class)))
            .isInstanceOf(ServletException.class)
            .hasMessage("해당하는 HandlerMapping이 없습니다.");
    }
}
