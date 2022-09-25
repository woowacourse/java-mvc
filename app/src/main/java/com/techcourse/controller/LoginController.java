package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private static final String REDIRECT_INDEX_JSP_VIEW_NAME = "redirect:/index.jsp";
    private static final String REDIRECT_UNAUTHORIZED_JSP_VIEW_NAME = "redirect:/401.jsp";
    private static final String LOGIN_REQUEST_PARAMETER_ACCOUNT_KEY = "account";
    private static final String LOGIN_REQUEST_PARAMETER_PASSWORD_KEY = "password";


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest request, final HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView(REDIRECT_INDEX_JSP_VIEW_NAME));
        }

        return InMemoryUserRepository.findByAccount(request.getParameter(LOGIN_REQUEST_PARAMETER_ACCOUNT_KEY))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(request, user);
                })
                .orElse(new ModelAndView(new JspView(REDIRECT_UNAUTHORIZED_JSP_VIEW_NAME)));
    }

    private ModelAndView login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter(LOGIN_REQUEST_PARAMETER_PASSWORD_KEY))) {
            HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new ModelAndView(new JspView(REDIRECT_INDEX_JSP_VIEW_NAME));
        } else {
            return new ModelAndView(new JspView(REDIRECT_UNAUTHORIZED_JSP_VIEW_NAME));
        }
    }
}
