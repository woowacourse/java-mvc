package nextstep.mvc;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import nextstep.web.support.StatusCode;

public class FileViewUtils {

    public static FileOption render(HttpServletRequest request, HttpServletResponse response) {
        return new FileOption(request, response);
    }

    public static class FileOption {

        private final HttpServletRequest request;
        private final HttpServletResponse response;
        private final List<HeaderPair> headers = new ArrayList<>();
        private String path;
        private StatusCode statusCode;

        public FileOption(HttpServletRequest request, HttpServletResponse response) {
            this.request = request;
            this.response = response;
            this.statusCode = StatusCode.OK;
        }

        public FileOption path(String path) {
            this.path = path;
            return this;
        }

        public FileOption statusCode(StatusCode statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public FileOption header(String key, String value) {
            headers.add(new HeaderPair(key, value));
            return this;
        }

        public void flush() throws IOException, ServletException {
            final RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
            response.setCharacterEncoding("UTF-8");
            response.setStatus(statusCode.statusNumber());
            for (HeaderPair header : headers) {
                response.addHeader(header.key, header.value);
            }
            requestDispatcher.forward(request, response);
        }

        private static class HeaderPair {
            private final String key;
            private final String value;

            public HeaderPair(String key, String value) {
                this.key = key;
                this.value = value;
            }
        }
    }
}
