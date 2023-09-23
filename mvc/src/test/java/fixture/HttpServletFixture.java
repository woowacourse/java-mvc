package fixture;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

public class HttpServletFixture {

    public static HttpServletRequest httpServletRequest(
            final String uri,
            final String method,
            final Map<String, String> attributes
    ) {
        final var request = mock(HttpServletRequest.class);

        for (final String key : attributes.keySet()) {
            given(request.getAttribute(key)).willReturn(attributes.get(key));
        }
        given(request.getRequestURI()).willReturn(uri);
        given(request.getMethod()).willReturn(method);

        return request;
    }
}
