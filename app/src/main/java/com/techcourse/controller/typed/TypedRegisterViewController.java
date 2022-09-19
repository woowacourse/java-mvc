package com.techcourse.controller.typed;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;

public class TypedRegisterViewController implements Controller {

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) {
        return "/register.jsp";
    }
}
