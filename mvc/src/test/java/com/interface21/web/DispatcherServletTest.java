package com.interface21.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

public class DispatcherServletTest {

    @Test
    @DisplayName("init()으로 초기화 해주지 않으면 service() 할 수 없다.")
    void init_mustNeeded() {
        // given
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // when & then
        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("TestController의 GET 요청을 처리할 수 있다.")
    void service_getRequest() throws ServletException {
        // given
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
        HttpServletRequest request = new MockHttpServletRequest("GET", "/get-test");
        request.setAttribute("id", 1);

        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        dispatcherServlet.service(request, response);

        // then
        assertThat(response.getForwardedUrl()).isEqualTo("/get.jsp");
    }

    @Test
    @DisplayName("TestController의 POST 요청을 처리할 수 있다.")
    void service_postRequest() throws ServletException {
        // given
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
        HttpServletRequest request = new MockHttpServletRequest("POST", "/post-test");
        request.setAttribute("id", 1);

        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        dispatcherServlet.service(request, response);

        // then
        assertThat(response.getForwardedUrl()).isEqualTo("/post.jsp");
    }
}
