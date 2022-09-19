package nextstep.mvc.controller.tobe;

import java.util.ArrayList;
import java.util.List;
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

    public static List<HandlerKey> from(final RequestMapping requestMapping) {
        final List<HandlerKey> handlerKeys = new ArrayList<>();
        for (RequestMethod method : requestMapping.method()) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), method);
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
