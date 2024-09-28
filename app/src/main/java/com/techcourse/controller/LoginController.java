package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private static final String REDIRECT_INDEX_JSP = "redirect:/index.jsp";
    private static final String REDIRECT_401_JSP = "redirect:/401.jsp";
    private static final String LOGIN_JSP = "/login.jsp";

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest req, HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return redirectTo(REDIRECT_INDEX_JSP);
        }

        String account = req.getParameter("account");
        String password = req.getParameter("password");

        return InMemoryUserRepository.findByAccount(account)
                .filter(user -> authenticateUser(req, user, password))
                .map(user -> {
                    log.info("User : {}", user);
                    return redirectTo(REDIRECT_INDEX_JSP);
                })
                .orElseGet(() -> redirectTo(REDIRECT_401_JSP));
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req, HttpServletResponse res) throws Exception {
        return UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return redirectTo(REDIRECT_INDEX_JSP);
                })
                .orElseGet(() -> {
                    JspView view = new JspView(LOGIN_JSP);
                    return new ModelAndView(view);
                });
    }

    private boolean authenticateUser(HttpServletRequest request, User user, String password) {
        if (user.checkPassword(password)) {
            request.getSession().setAttribute(UserSession.SESSION_KEY, user);
            return true;
        }
        return false;
    }

    public ModelAndView redirectTo(String path) {
        View view = new JspView(path);
        return new ModelAndView(view);
    }
}
