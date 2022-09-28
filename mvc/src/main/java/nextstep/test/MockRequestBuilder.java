package nextstep.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class MockRequestBuilder {

    private final String url;
    private final String method;
    private final Map<String, String> attributes;
    private final Map<String, String> parameters;

    public MockRequestBuilder(final String url, final String method) {
        this.url = url;
        this.method = method;
        this.attributes = new HashMap<>();
        this.parameters = new HashMap<>();
    }

    public static MockRequestBuilder get(final String url) {
        return new MockRequestBuilder(url, "GET");
    }

    public static MockRequestBuilder post(final String url) {
        return new MockRequestBuilder(url, "POST");
    }

    public MockRequestBuilder attribute(final String key, final String value) {
        attributes.put(key, value);
        return this;
    }

    public MockRequestBuilder param(final String key, final String value) {
        parameters.put(key, value);
        return this;
    }

    public HttpServletRequest build() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        final HttpSession session = mock(HttpSession.class);

        when(request.getRequestURI()).thenReturn(url);
        when(request.getMethod()).thenReturn(method);
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);

        for (String key : attributes.keySet()) {
            when(request.getAttribute(key)).thenReturn(attributes.get(key));
        }

        for (String key : parameters.keySet()) {
            when(request.getParameter(key)).thenReturn(parameters.get(key));
        }

        return request;
    }
}
