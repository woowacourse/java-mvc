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

    public static List<HandlerKey> buildFrom(final RequestMapping requestMapping) {
        return Arrays.stream(requestMapping.method())
                .map(method -> new HandlerKey(requestMapping.value(), method))
                .toList();
    }

    public static List<HandlerKey> buildWithAllMethodsFrom(RequestMapping requestMapping) {
        return Arrays.stream(RequestMethod.values())
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
