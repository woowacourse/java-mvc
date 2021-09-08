package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.exception.DuplicateException;
import com.techcourse.service.RegisterService;
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
public class RegisterController {

    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);

    private final RegisterService registerService;

    public RegisterController() {
        this.registerService = new RegisterService();
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req, HttpServletResponse res) {
        log.info("RegisterController GET method");
        return new ModelAndView(new JspView("/register.jsp"));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(HttpServletRequest req, HttpServletResponse res) {
        log.info("RegisterController POST method");
        try {
            final User user = registerService.register(
                    req.getParameter("account"),
                    req.getParameter("password"),
                    req.getParameter("email")
            );
            final HttpSession session = req.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
        } catch (DuplicateException e) {
            log.info("account 또는 email 중복으로 인한 회원가입 실패");
        }
        return new ModelAndView(new JspView("redirect:/index.jsp"));
    }
}

