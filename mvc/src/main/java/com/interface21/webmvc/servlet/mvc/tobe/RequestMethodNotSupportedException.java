package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import java.util.Set;

public class RequestMethodNotSupportedException extends RuntimeException {

    private final String method;
    private final Set<RequestMethod> supportedMethods;

    public RequestMethodNotSupportedException(final String method, final Set<RequestMethod> supportedMethods) {
        super("Request method '" + method + "' is not supported. Supported methods: " + supportedMethods);
        this.method = method;
        this.supportedMethods = Set.copyOf(supportedMethods);
    }

    public String getMethod() {
        return method;
    }

    public Set<RequestMethod> getSupportedMethods() {
        return supportedMethods;
    }
}
