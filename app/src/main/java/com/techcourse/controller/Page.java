package com.techcourse.controller;

import java.util.function.Function;

import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;

public enum Page {

    INDEX("index.jsp", JspView::new),
    LOGIN("login.jsp", JspView::new),
    REGISTER("register.jsp", JspView::new),
    ERROR_401("401.jsp", JspView::new),
    ERROR_404("404.jsp", JspView::new),
    ;

    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String BASE_PATH = "/";

    private final Function<String, View> viewConvertor;
    private final String filePath;

    Page(final String fileName, final Function<String, View> viewConvertor) {
        this.viewConvertor = viewConvertor;
        this.filePath = BASE_PATH + fileName;
    }

    public ModelAndView getModelAndView() {
        final View view = viewConvertor.apply(filePath);
        return new ModelAndView(view);
    }

    public ModelAndView getRedirectModelAndView() {
        final String redirectFilePath = REDIRECT_PREFIX + filePath;
        final View view = viewConvertor.apply(redirectFilePath);
        return new ModelAndView(view);
    }
}
