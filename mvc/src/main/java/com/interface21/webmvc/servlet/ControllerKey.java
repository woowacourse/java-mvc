package com.interface21.webmvc.servlet;

import com.interface21.web.bind.annotation.RequestMethod;

public record ControllerKey(String uri, RequestMethod method) {

}
