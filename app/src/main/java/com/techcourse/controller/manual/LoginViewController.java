package com.techcourse.controller.manual;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.techcourse.service.AuthService;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginViewController implements Controller {

    private AuthService authService = new AuthService();

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        try {
            authService.loginWithSession(req.getSession());
            return "redirect:/index.jsp";

        } catch (AuthException exception) {
            return "/login.jsp";
        }
    }
}
