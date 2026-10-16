package com.interface21.webmvc.servlet;

import com.interface21.web.http.MediaType;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class DispatcherServletTest {

    @Test
    void 생성자로_전달한_패키지의_컨트롤러를_실행한다() throws Exception {
        final Servlet servlet = new DispatcherServlet("dispatching");
        servlet.init(mock(ServletConfig.class));
        final var request = new MockHttpServletRequest("GET", "/dispatcher-test");
        final var response = new MockHttpServletResponse();
        request.addParameter("message", "안녕하세요");

        servlet.service(request, response);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(response.getContentAsString()).isEqualTo("\"안녕하세요\"");
    }

    @Test
    void 지정한_패키지_밖의_컨트롤러는_등록하지_않는다() throws Exception {
        final Servlet servlet = new DispatcherServlet("samples");
        servlet.init(mock(ServletConfig.class));
        final var request = new MockHttpServletRequest("GET", "/dispatcher-test");
        final var response = new MockHttpServletResponse();

        servlet.service(request, response);

        assertThat(response.getStatus()).isEqualTo(404);
    }
}
