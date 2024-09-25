package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerKey {

    private static final List<HandlerKey> CACHE = new ArrayList<>();

    public final String url;
    public final RequestMethod requestMethod;

    public static HandlerKey from(String url, RequestMethod requestMethod) {
        return CACHE.stream()
                .filter(handlerKey -> handlerKey.url.equals(url) && handlerKey.requestMethod == requestMethod)
                .findAny()
                .orElseGet(() -> new HandlerKey(url, requestMethod));
    }

    private HandlerKey(final String url, final RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
        CACHE.add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HandlerKey)) {
            return false;
        }
        HandlerKey handlerKey = (HandlerKey) o;
        return Objects.equals(url, handlerKey.url) && requestMethod == handlerKey.requestMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, requestMethod);
    }

    @Override
    public String toString() {
        return "HandlerKey{" +
                "url='" + url + '\'' +
                ", requestMethod=" + requestMethod +
                '}';
    }
}
