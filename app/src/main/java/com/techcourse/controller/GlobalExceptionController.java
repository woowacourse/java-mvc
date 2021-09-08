package com.techcourse.controller;

import com.techcourse.exception.UnauthorizedException;
import com.techcourse.exception.UserException;
import nextstep.mvc.exception.UnHandledRequestException;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.ExceptionHandler;

@Controller
public class GlobalExceptionController {

    @ExceptionHandler(UnauthorizedException.class)
    public ModelAndView unauthorizedExceptionHandler(Exception e) {
        return new ModelAndView("redirect:/401.jsp");
    }

    @ExceptionHandler(UserException.class)
    public ModelAndView userException(Exception e) {
        return new ModelAndView("redirect:/400.jsp");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView unhandledServerException(Exception e) {
        return new ModelAndView("redirect:/500.jsp");
    }

    @ExceptionHandler(UnHandledRequestException.class)
    public ModelAndView notFoundException(Exception e) {
        return new ModelAndView("redirect:/404.jsp");
    }
}
