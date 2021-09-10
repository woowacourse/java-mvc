package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Objects;
import nextstep.web.support.RequestMethod;

public class HandlerKey {

    private final String url;
    private final RequestMethod requestMethod;

    public HandlerKey(String url, RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    public static HandlerKey createByRequest(final HttpServletRequest request) {
        return new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod().toUpperCase(Locale.ROOT)));
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
        if (this == o) {
            return true;
        }
        if (!(o instanceof HandlerKey)) {
            return false;
        }
        HandlerKey that = (HandlerKey) o;
        return Objects.equals(url, that.url) && requestMethod == that.requestMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, requestMethod);
    }
}
