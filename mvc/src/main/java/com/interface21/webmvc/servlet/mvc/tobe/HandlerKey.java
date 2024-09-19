package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class HandlerKey {

    private final String url;
    private final RequestMethod requestMethod;

    public HandlerKey(final String url, final RequestMethod requestMethod) {
        validateUrlForm(url);
        this.url = url;
        this.requestMethod = requestMethod;
    }

    private void validateUrlForm(String url) {
        if (!url.startsWith("/")) {
            throw new IllegalArgumentException("잘못된 형식의 url 입니다. url 양식 [%s]".formatted(url));
        }

        try {
            new URI(url);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("잘못된 형식의 url 입니다. url 양식 [%s]".formatted(url));
        }
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
