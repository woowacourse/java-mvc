package nextstep.mvc.controller.tobe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import nextstep.web.support.RequestMethod;

public class HandlerKey {

    private final String url;
    private final List<RequestMethod> requestMethod;

    public HandlerKey(String url, RequestMethod... method) {
        this(url, Arrays.asList(method));
    }

    public HandlerKey(String url, List<RequestMethod> requestMethod) {
        this.url = url;
        this.requestMethod = new ArrayList<>(requestMethod);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof HandlerKey))
            return false;
        HandlerKey that = (HandlerKey) o;
        return Objects.equals(url, that.url) && Objects.equals(requestMethod, that.requestMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, requestMethod);
    }
}
