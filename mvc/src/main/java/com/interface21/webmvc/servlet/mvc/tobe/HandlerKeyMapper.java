package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.ArrayList;
import java.util.List;

import com.interface21.web.bind.annotation.RequestMethod;

public class HandlerKeyMapper {

    public List<HandlerKey> mapHandlerKeys(String url, RequestMethod[] requestMethods) {
        List<HandlerKey> handlerKeys = new ArrayList<>();
        for (RequestMethod requestMethod : requestMethods) {
            handlerKeys.add(new HandlerKey(url, requestMethod));
        }
        return handlerKeys;
    }
}
