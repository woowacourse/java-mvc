package nextstep.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import nextstep.mvc.AnnotationHandlerAdapter;
import nextstep.mvc.ControllerHandlerAdapter;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.HandlerMapping;

public class MockMvc {

    private final DispatcherServlet dispatcherServlet;

    public MockMvc(final HandlerMapping... handlerMappings) {
        this.dispatcherServlet = new DispatcherServlet();

        for (HandlerMapping handlerMapping : handlerMappings) {
            dispatcherServlet.addHandlerMapping(handlerMapping);
        }

        dispatcherServlet.addHandlerAdapter(new ControllerHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new AnnotationHandlerAdapter());
        dispatcherServlet.init();
    }

    public ResultMatcher perform(final String url, final String method) throws Exception {
        return perform(url, method, Map.of());
    }

    public ResultMatcher perform(final String url, final String method, final Map<String, Object> attribute)
            throws Exception {
        HttpServletRequest request = createMockedRequest(url, method, attribute);
        HttpServletResponse response = createMockedResponse();

        dispatcherServlet.service(request, response);

        return new ResultMatcher(request, response);
    }

    private HttpServletRequest createMockedRequest(
            final String url, final String method, final Map<String, Object> attributes
    ) {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn(url);
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);

        for (String key : attributes.keySet()) {
            when(request.getAttribute(key)).thenReturn(attributes.get(key));
        }

        return request;
    }

    private HttpServletResponse createMockedResponse() throws IOException {
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter writer = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(writer);

        return response;
    }
}
