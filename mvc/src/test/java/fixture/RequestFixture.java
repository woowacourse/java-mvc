package fixture;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;

public class RequestFixture {

    private RequestFixture() {
    }

    public static HttpServletRequest getRequest() {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        given(request.getAttribute("id")).willReturn("gugu");
        given(request.getRequestURI()).willReturn("/get-test");
        given(request.getMethod()).willReturn("GET");

        return request;
    }

    public static HttpServletRequest getRequest(final String path) {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        given(request.getAttribute("id")).willReturn("gugu");
        given(request.getRequestURI()).willReturn(path);
        given(request.getMethod()).willReturn("GET");

        return request;
    }

    public static HttpServletRequest postRequest() {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        given(request.getAttribute("id")).willReturn("gugu");
        given(request.getRequestURI()).willReturn("/post-test");
        given(request.getMethod()).willReturn("POST");

        return request;
    }
}
