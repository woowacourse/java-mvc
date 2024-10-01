package com.interface21.webmvc.mapping;

import com.interface21.webmvc.handler.RequestHandler;
import java.io.FileNotFoundException;

public interface HandlerMapping {

    void initialize() throws ReflectiveOperationException, FileNotFoundException;

    RequestHandler getHandler(final String requestMethod, final String requestURI);

    boolean canHandle(final String requestMethod, final String requestURI);
}
