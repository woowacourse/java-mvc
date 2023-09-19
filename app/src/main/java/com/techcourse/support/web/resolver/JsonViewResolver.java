package com.techcourse.support.web.resolver;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.view.JsonView;
import webmvc.org.springframework.web.servlet.view.resolver.ViewResolver;

public class JsonViewResolver implements ViewResolver {

    private static final String ACCEPT_HEADER_KET = "Accept";
    private static final String APPLICATION_JSON_VALUE = "application/json";

    @Override
    public boolean supports(final HttpServletRequest request, final String ignored) {
        final String acceptHeaderValue = request.getHeader(ACCEPT_HEADER_KET);

        return acceptHeaderValue == null || acceptHeaderValue.contains(APPLICATION_JSON_VALUE);
    }

    @Override
    public View resolve(final String ignored) {
        return new JsonView();
    }
}
