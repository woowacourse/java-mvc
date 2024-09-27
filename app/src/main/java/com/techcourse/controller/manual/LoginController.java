package com.techcourse.controller.manual;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.techcourse.service.AuthService;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginController implements Controller {

    private AuthService authService = new AuthService();

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        try {
            authService.login(req.getParameter("account"), req.getParameter("password"), req.getSession());
            return "redirect:/index.jsp";

        } catch (AuthException exception) {
            return "redirect:/401.jsp";
        }
    }
}
