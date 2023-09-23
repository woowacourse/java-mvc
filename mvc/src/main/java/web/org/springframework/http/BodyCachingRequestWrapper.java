package web.org.springframework.http;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BodyCachingRequestWrapper extends HttpServletRequestWrapper {

    private final String body;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request the {@link HttpServletRequest} to be wrapped.
     * @throws IllegalArgumentException if the request is null
     */
    public BodyCachingRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = cacheBody(request);
    }

    private String cacheBody(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        if (inputStream == null) {
            throw new IllegalStateException();
        }

        return readBody(inputStream);
    }

    private String readBody(InputStream inputStream) throws IOException {
        final var bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        char[] charBuffer = new char[128];
        int bytesRead = -1;

        try (inputStream; bufferedReader) {
            StringBuilder stringBuilder = new StringBuilder();
            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                stringBuilder.append(charBuffer, 0, bytesRead);
            }
            return stringBuilder.toString();
        }
    }


    @Override
    public ServletInputStream getInputStream() {
        return new CachedBodyInputStream(body.getBytes());
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    public String getBody() {
        return this.body;
    }

}
