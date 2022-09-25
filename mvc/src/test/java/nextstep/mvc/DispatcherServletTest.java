package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecutionHandlerAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        dispatcherServlet.addAdapter(new HandlerExecutionHandlerAdapter());
        dispatcherServlet.init();
    }

    @Test
    void get_test() throws ServletException, IOException {
        // given & setup
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        HttpServletRequest request = MockHttpRequestBuilder.builder()
                .mockRequestURI("/get-test")
                .mockMethod("GET")
                .mockRequestDispatcher(requestDispatcher, "/get.jsp")
                .build();
        HttpServletResponse response = mock(HttpServletResponse.class);

        // when
        dispatcherServlet.service(request, response);

        // then
        verify(request.getRequestDispatcher("/get.jsp")).forward(request, response);
    }

    @Test
    void post_test() throws ServletException, IOException {
        // given & setup
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        HttpServletRequest request = MockHttpRequestBuilder.builder()
                .mockRequestURI("/post-test")
                .mockMethod("POST")
                .mockRequestDispatcher(requestDispatcher, "/post.jsp")
                .build();
        HttpServletResponse response = mock(HttpServletResponse.class);

        // when
        dispatcherServlet.service(request, response);

        // then
        verify(request.getRequestDispatcher("/post.jsp")).forward(request, response);
    }

    @Test
    void getHandlerException() {
        HttpServletRequest request = MockHttpRequestBuilder.builder()
                .mockRequestURI("/not-found")
                .mockMethod("POST")
                .build();
        HttpServletResponse response = mock(HttpServletResponse.class);
        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasMessage("요청을 처리할 수 없습니다.");

    }

    private static class MockHttpRequestBuilder {

        private String requestURI;
        private String method;
        private RequestDispatcher requestDispatcher;
        private String requestDispatcherPath;

        private MockHttpRequestBuilder() {
        }

        private static MockHttpRequestBuilder builder() {
            return new MockHttpRequestBuilder();
        }

        public MockHttpRequestBuilder mockRequestURI(final String requestURI) {
            this.requestURI = requestURI;
            return this;
        }

        public MockHttpRequestBuilder mockMethod(final String method) {
            this.method = method;
            return this;
        }

        public MockHttpRequestBuilder mockRequestDispatcher(final RequestDispatcher requestDispatcher,
                                                            final String requestDispatcherPath) {
            this.requestDispatcher = requestDispatcher;
            this.requestDispatcherPath = requestDispatcherPath;
            return this;
        }

        public HttpServletRequest build() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getRequestURI()).thenReturn(requestURI);
            when(request.getMethod()).thenReturn(method);
            when(request.getRequestDispatcher(requestDispatcherPath)).thenReturn(requestDispatcher);
            return request;
        }
    }
}
