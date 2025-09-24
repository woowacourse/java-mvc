package com.interface21.webmvc.servlet.mvc.tobe.exception;

import java.util.Set;

public class MethodNotAllowedException extends RuntimeException {

    public MethodNotAllowedException(String path, Set<String> allowedMethods) {
        super("Method not allowed: " + allowedMethods + " /" + path);
    }
}
