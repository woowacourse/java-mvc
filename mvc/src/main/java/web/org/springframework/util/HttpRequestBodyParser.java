package web.org.springframework.util;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

public class HttpRequestBodyParser {

    public static String parse(final HttpServletRequest request) throws IOException {
        return request.getReader()
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
