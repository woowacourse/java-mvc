package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;

public interface HandlerMapping {

    Object getHandler(String path, RequestMethod method);
}
