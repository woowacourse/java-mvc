package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import java.util.ArrayList;
import java.util.List;

public class HandlerKeys {

    private static final List<HandlerKey> handlerKeys = new ArrayList<>();

    public HandlerKeys(RequestMapping requestMapping) {
        String value = requestMapping.value();
        RequestMethod[] requestMethods = getRequestMethods(requestMapping);
        for (RequestMethod requestMethod : requestMethods) {
            handlerKeys.add(new HandlerKey(value, requestMethod));
        }
    }

    private RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestedMethods = requestMapping.method();
        if (requestedMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestedMethods;
    }

    public List<HandlerKey> getHandlerKeys() {
        return handlerKeys;
    }
}
