package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;

import java.util.Objects;

public record HandlerKey(
        String url,
        RequestMethod requestMethod
) {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HandlerKey(String otherUrl, RequestMethod method))) {
            return false;
        }
        return Objects.equals(url, otherUrl)
                && requestMethod == method;
    }
}
