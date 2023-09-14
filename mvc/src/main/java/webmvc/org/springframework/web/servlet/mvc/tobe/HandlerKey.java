package webmvc.org.springframework.web.servlet.mvc.tobe;

import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.exception.HandlerKeyException;

import java.util.Objects;

import static java.util.Objects.*;

public class HandlerKey {

    private final String url;
    private final RequestMethod requestMethod;

    public HandlerKey(final String url, final RequestMethod requestMethod) {
        if (isNull(url)) {
            throw new HandlerKeyException("[ERROR] HandlerKey 를 생성할 때 url 이 null 일 수 없습니다.");
        }
        if (isNull(requestMethod)) {
            throw new HandlerKeyException("[ERROR] HandlerKey 를 생성할 때 requestMethod 는 null 일 수 없습니다.");
        }
        this.url = url;
        this.requestMethod = requestMethod;
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
        return hash(url, requestMethod);
    }

    @Override
    public String toString() {
        return "HandlerKey{" +
               "url='" + url + '\'' +
               ", requestMethod=" + requestMethod +
               '}';
    }
}
