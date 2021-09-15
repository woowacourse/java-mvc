package com.techcourse.controller.annotation;

import static nextstep.web.support.RequestMethod.POST;

import com.techcourse.controller.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LogoutController {

    private static final Logger LOG = LoggerFactory.getLogger(LogoutController.class);
    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String HOME_PATH = "/index";

    @RequestMapping(value = "/logout", method = POST)
    public ModelAndView postLogout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);

        LOG.info("Logout Success!");

        return new ModelAndView(new JspView(REDIRECT_PREFIX + HOME_PATH));
    }
}
