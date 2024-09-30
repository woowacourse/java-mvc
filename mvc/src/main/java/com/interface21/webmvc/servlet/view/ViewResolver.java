package com.interface21.webmvc.servlet.view;

import java.util.Objects;

import jakarta.servlet.http.HttpServletResponse;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;

public class ViewResolver {

    private final ModelAndView modelAndView;

    public ViewResolver(final ModelAndView modelAndView) {
        this.modelAndView = modelAndView;
    }

    public View resolve(final HttpServletResponse response) {
        if (Objects.nonNull(response.getContentType()) && response.getContentType()
                .equals(MediaType.APPLICATION_JSON_UTF8_VALUE)) {
            return new JsonView();
        }
        return modelAndView.getView();
    }
}
