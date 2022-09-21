package com.techcourse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextstep.web.support.RequestMethod;

public class LogUtil {

    private static final Logger log = LoggerFactory.getLogger(LogUtil.class);

    public static void requestInfo(String uri, RequestMethod method) {
        info("uri: {}, method: {}", uri, method);
    }

    public static void info(String message, Object... parameters) {
        log.info(message, parameters);
    }
}
