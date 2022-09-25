package fixture;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;

public class RequestFixture {

    private RequestFixture() {
    }

    public static HttpServletRequest getRequest() {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        return request;
    }

    public static HttpServletRequest getRequest(final String path) {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn(path);
        when(request.getMethod()).thenReturn("GET");

        return request;
    }

    public static HttpServletRequest postRequest() {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        return request;
    }
}
