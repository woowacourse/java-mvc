package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;

public record HandlerKey(String url, RequestMethod requestMethod) {

}
