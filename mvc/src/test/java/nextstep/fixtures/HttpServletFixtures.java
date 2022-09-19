package nextstep.fixtures;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HttpServletFixtures {

    public static HttpServletRequest createRequest() {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        return request;

    }

    public static HttpServletResponse createResponse() {
        return mock(HttpServletResponse.class);
    }
}
