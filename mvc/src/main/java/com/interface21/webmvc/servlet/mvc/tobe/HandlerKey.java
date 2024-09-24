package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class HandlerKey {

    private static final Logger log = LoggerFactory.getLogger(HandlerKey.class);

    private final String url;
    private final RequestMethod requestMethod;

    public HandlerKey(final HttpServletRequest request) {
        this.url = request.getRequestURI();
        this.requestMethod = RequestMethod.from(request.getMethod());
    }

    public HandlerKey(final String url, final RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
        log.info("Url : {}, HTTP Method : {}",url,requestMethod);
    }

    public HandlerKey(final String url) {
        this.url = url;
        this.requestMethod = RequestMethod.GET;
    }

    @Override
    public String toString() {
        return "HandlerKey{" +
                "url='" + url + '\'' +
                ", requestMethod=" + requestMethod +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof HandlerKey that)) return false;
        return Objects.equals(url, that.url) && requestMethod == that.requestMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, requestMethod);
    }
}
