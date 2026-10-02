package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import java.util.Arrays;
import java.util.List;

public class RequestMappingInfo {

    private final String url;
    private final RequestMethod[] requestMethods;

    public RequestMappingInfo(final RequestMapping requestMapping) {
        this.url = requestMapping.value();
        this.requestMethods = requestMapping.method();
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
