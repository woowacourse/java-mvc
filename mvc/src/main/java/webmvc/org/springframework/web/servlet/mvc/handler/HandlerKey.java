package webmvc.org.springframework.web.servlet.mvc.handler;

import jakarta.servlet.http.HttpServletRequest;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.util.Objects;

public class HandlerKey {

    public static final String QUERY_STRING_SEPARATOR = "?";
    private final String url;
    private final RequestMethod requestMethod;

    public HandlerKey(final String url, final RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    public boolean isMatching(final HttpServletRequest request) {
        String path = getPath(request);
        return Objects.equals(path, url)
                && requestMethod.isMatching(request);
    }

    private String getPath(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        if (requestURI.contains(QUERY_STRING_SEPARATOR)) {
            return requestURI.substring(0, requestURI.indexOf(QUERY_STRING_SEPARATOR));
        }
        return requestURI;
    }

    @Override
    public String toString() {
        return "HandlerKey{" +
                "url='" + url + '\'' +
                ", requestMethod=" + requestMethod +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HandlerKey)) return false;
        HandlerKey that = (HandlerKey) o;
        return Objects.equals(url, that.url) && requestMethod == that.requestMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, requestMethod);
    }
}
