package com.techcourse.controller.annotation;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.service.AuthService;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService = new AuthService();

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginView(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            authService.loginWithSession(request.getSession());
            return resolveModelAndView("redirect:/index.jsp");
        } catch (AuthException exception) {
            return resolveModelAndView("/login.jsp");
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            authService.login(
                    request.getParameter("account"),
                    request.getParameter("password"),
                    request.getSession()
            );
            return resolveModelAndView("redirect:/index.jsp");

        } catch (AuthException exception) {
            return resolveModelAndView("redirect:/401.jsp");
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
        authService.logout(request.getSession());
        return resolveModelAndView("redirect:/");
    }

    private ModelAndView resolveModelAndView(String viewName) {
        View view = new JspView(viewName);
        return new ModelAndView(view);
    }
}
