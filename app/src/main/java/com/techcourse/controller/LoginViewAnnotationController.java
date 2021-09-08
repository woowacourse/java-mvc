package com.techcourse.controller;

import air.annotation.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class LoginViewAnnotationController {

    @RequestMapping(value = "/login/view/v2", method = RequestMethod.GET)
    public String loginView(HttpServletRequest request, HttpServletResponse response) {
        return UserSession.getUserFrom(request.getSession())
                          .map(user -> "redirect:/index.jsp")
                          .orElse("/login.jsp");
    }
}
