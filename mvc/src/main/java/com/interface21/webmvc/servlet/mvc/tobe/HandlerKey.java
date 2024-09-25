package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class HandlerKey {

    private static final Map<String, HandlerKey> CACHE = new ConcurrentHashMap<>();

    public final String url;
    public final RequestMethod requestMethod;

    public static HandlerKey from(String url, RequestMethod requestMethod) {
        String key = url + requestMethod.name();
        if (CACHE.containsKey(key)) {
            return CACHE.get(key);
        }
        return new HandlerKey(url, requestMethod);
    }

    private HandlerKey(final String url, final RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
        CACHE.put(url + requestMethod.name(), this);
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
