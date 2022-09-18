package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

public class HandlerKey {

    private final String url;
    private final RequestMethod requestMethod;

    public HandlerKey(final String url, final RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    public static HandlerKey from(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        RequestMethod requestMethod = RequestMethod.valueOf(method);
        return new HandlerKey(requestURI, requestMethod);
    }

    public static HandlerKey from(final Method handlerMethod) {
        RequestMapping requestMapping = handlerMethod.getAnnotation(RequestMapping.class);
        String requestURI = requestMapping.value();
        RequestMethod requestMethod = requestMapping.method()[0];
        return new HandlerKey(requestURI, requestMethod);
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
