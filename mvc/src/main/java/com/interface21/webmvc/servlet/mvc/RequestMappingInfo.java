package com.interface21.webmvc.servlet.mvc;

import java.util.Arrays;
import java.util.List;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

public class RequestMappingInfo {

    private final RequestMapping requestMapping;

    public RequestMappingInfo(RequestMapping requestMapping) {
        this.requestMapping = requestMapping;
    }

    public List<HandlerKey> getHandlerKeys() {
        RequestMethod[] methods = getHttpMethods();
        return Arrays.stream(methods)
                .map(method -> new HandlerKey(requestMapping.value(), method))
                .toList();
    }

    private RequestMethod[] getHttpMethods() {
        RequestMethod[] methods = requestMapping.method();
        if (methods.length == 0) {
            methods = RequestMethod.values();
        }

        return methods;
    }
}
