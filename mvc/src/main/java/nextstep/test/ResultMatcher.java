package nextstep.test;

import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.web.support.MediaType;

public class ResultMatcher {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public ResultMatcher(final HttpServletRequest request, final HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public ResultMatcher forwardTo(final String url) throws Exception {
        return forwardTo(url, Map.of());
    }

    public ResultMatcher forwardTo(final String url, final Map<String, String> attributes) throws Exception {
        verify(request).getRequestDispatcher(url);
        verify(request.getRequestDispatcher(url)).forward(request, response);

        for (String key : attributes.keySet()) {
            final String value = attributes.get(key);
            verify(request).setAttribute(key, value);
        }

        return this;
    }

    public ResultMatcher redirectTo(final String url) throws Exception {
        verify(response).sendRedirect(url);
        return this;
    }

    public ResultMatcher jsonBody(String body) throws Exception {
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        verify(response.getWriter()).write(body);
        return this;
    }
}
