package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final String REDIRECT_INDEX_JSP = "redirect:/index.jsp";
    private static final String REDIRECT_UNAUTHORIZED_JSP = "redirect:/401.jsp";
    private static final String REDIRECT_LOGIN_JSP = "/login.jsp";

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView getLogin(HttpServletRequest request, HttpServletResponse response) {
        return UserSession.getUserFrom(request.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return convertStringToMav(REDIRECT_INDEX_JSP);
                })
                .orElse(convertStringToMav(REDIRECT_LOGIN_JSP));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView postLogin(HttpServletRequest request, HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return convertStringToMav(REDIRECT_INDEX_JSP);
        }

        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(request, user);
                })
                .orElse(convertStringToMav(REDIRECT_UNAUTHORIZED_JSP));
    }

    private ModelAndView login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return convertStringToMav(REDIRECT_INDEX_JSP);
        }
        return convertStringToMav(REDIRECT_UNAUTHORIZED_JSP);
    }

    private ModelAndView convertStringToMav(String viewName) {
        return new ModelAndView(new JspView(viewName));
    }
}
