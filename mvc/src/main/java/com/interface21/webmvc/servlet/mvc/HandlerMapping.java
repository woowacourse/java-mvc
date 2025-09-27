package com.interface21.webmvc.servlet.mvc;

import com.interface21.web.bind.annotation.RequestMethod;

public interface HandlerMapping {

    void initialize();

    Object getHandler(String path, RequestMethod method);
}
