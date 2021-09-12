package nextstep.mvc.controller.tobe;

import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

import java.util.Arrays;
import java.util.Objects;

public class HandlerKey {

    private final String url;
    private final RequestMethod requestMethod;

    public HandlerKey(String url, RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    public boolean match(RequestMapping annotation) {
        return isUrlMatch(annotation) && isMethodMatch(annotation);
    }

    private boolean isUrlMatch(RequestMapping annotation) {
        return Objects.equals(url, annotation.value());
    }

    private boolean isMethodMatch(RequestMapping annotation) {
        return Arrays.asList(annotation.method()).contains(requestMethod);
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
