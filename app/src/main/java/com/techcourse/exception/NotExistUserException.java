package com.techcourse.exception;

import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JsonView;

public final class NotExistUserException {

    public static ModelAndView createJsonMessage() {
        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        final ExceptionMessage message = new ExceptionMessage("[ERROR] 존재하지 않는 유저입니다.");
        modelAndView.addObject("error", message);
        return modelAndView;
    }
}
