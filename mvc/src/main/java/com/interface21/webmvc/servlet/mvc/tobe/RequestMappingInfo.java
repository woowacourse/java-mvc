package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import java.util.Arrays;
import java.util.List;

public class RequestMappingInfo {

    private final String url;
    private final RequestMethod[] requestMethods;

    public RequestMappingInfo(final String url, final RequestMethod[] requestMethods) {
        this.url = url;
        this.requestMethods = requestMethods;
    }

    public List<HandlerKey> handlerKeys() {
        RequestMethod[] allowedMethods = requestMethods;

        if (allowedMethods.length == 0) {
            allowedMethods = RequestMethod.values();
        }

        return Arrays.stream(allowedMethods)
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .toList();
    }
}
