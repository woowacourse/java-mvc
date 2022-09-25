package fixture;

import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletResponse;

public class ResponseFixture {

    private ResponseFixture() {
    }

    public static HttpServletResponse response() {
        final HttpServletResponse response = mock(HttpServletResponse.class);

        return response;
    }
}
