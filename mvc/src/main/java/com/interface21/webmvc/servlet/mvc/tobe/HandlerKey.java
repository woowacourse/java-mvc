package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.interface21.web.bind.annotation.RequestMethod;

public class HandlerKey {
    private static Map<String, HandlerKey> instances = new HashMap<>();

    private final String url;
    private final RequestMethod requestMethod;

    private HandlerKey(final String url, final RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    public static HandlerKey of(String uri, RequestMethod requestMethod) {
        String key = uri + requestMethod.name();
        return instances.computeIfAbsent(key, k -> new HandlerKey(uri, requestMethod));
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
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        HandlerKey that = (HandlerKey) o;
        return Objects.equals(url, that.url) && requestMethod == that.requestMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, requestMethod);
    }
}
