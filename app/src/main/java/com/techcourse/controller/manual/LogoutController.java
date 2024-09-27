package com.techcourse.controller.manual;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.techcourse.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LogoutController implements Controller {

    private AuthService authService = new AuthService();

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        authService.logout(req.getSession());
        return "redirect:/";
    }
}
