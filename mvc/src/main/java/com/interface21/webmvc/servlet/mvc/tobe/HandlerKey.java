package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HandlerKey {

    public static final int FIRST_INDEX = 0;
    private final String url;
    private final RequestMethod requestMethod;

    public HandlerKey(final String url, final RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    public static HandlerKey from(final RequestMapping requestMapping) {
        return new HandlerKey(requestMapping.value(), requestMapping.method()[FIRST_INDEX]);
    }

    public static List<HandlerKey> listOf(RequestMapping requestMapping, RequestMethod[] methods) {
        return Arrays.stream(methods)
                .map(method -> new HandlerKey(requestMapping.value(), method))
                .toList();
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
