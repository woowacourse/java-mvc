package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import nextstep.web.support.RequestMethod;

public class HandlerKey {

    private final String url;
    private final RequestMethod requestMethod;

    public HandlerKey(final String url, final RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    public HandlerKey(final HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        this.url = url;
        this.requestMethod = requestMethod;
    }

    public static List<HandlerKey> from(final ControllerMethod controllerMethod) {
        String url = controllerMethod.getUrl();
        List<RequestMethod> methods = controllerMethod.getRequestMethods();

        List<HandlerKey> handlerKeys = new ArrayList<>();
        for (RequestMethod requestMethod : methods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            handlerKeys.add(handlerKey);
        }

        return handlerKeys;
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
