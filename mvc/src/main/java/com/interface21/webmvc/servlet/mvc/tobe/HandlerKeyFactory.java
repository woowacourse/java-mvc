package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import java.util.Arrays;
import java.util.List;

public class HandlerKeyFactory {

    public static List<HandlerKey> from(final RequestMapping requestMapping) {
        RequestMethod[] methods = requestMapping.method();
        if (methods.length == 0) {
            methods = RequestMethod.values();
        }

        return Arrays.stream(methods)
                .map(method -> new HandlerKey(requestMapping.value(), method))
                .toList();
    }
}
