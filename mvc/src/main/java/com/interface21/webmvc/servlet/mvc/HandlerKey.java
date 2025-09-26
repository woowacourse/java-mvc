package com.interface21.webmvc.servlet.mvc;

import com.interface21.web.bind.annotation.RequestMethod;

public record HandlerKey(
        String url,
        RequestMethod requestMethod
) {

}
