package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TestReflections {

    private static final String TEST_RESPONSE = "test.jsp";

    public String testMethod() {
        return TEST_RESPONSE;
    }

    public String handler(final HttpServletRequest request, final HttpServletResponse response) {
        return TEST_RESPONSE;
    }
}
