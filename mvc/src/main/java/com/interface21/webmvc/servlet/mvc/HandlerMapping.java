package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;

public interface HandlerMapping {
    void initialize();

    HandlerExecution getHandler(final HandlerKey handlerKey);
}
