package com.techcourse.exception;

import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JsonView;

public final class NotExistUserException {

    public static ModelAndView createJsonMessage() {
        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("error", "[ERROR] 존재하지 않는 유저 입니다.");
        return modelAndView;
    }
}
