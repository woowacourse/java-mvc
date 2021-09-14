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

    private static final Logger LOG = LoggerFactory.getLogger(RegisterController.class);

    private final RegisterService registerService;

    public RegisterController() {
        this.registerService = new RegisterService();
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("RegisterController GET method");
        return new ModelAndView(new JspView("/register.jsp"));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("RegisterController POST method");
        try {
            final User user = registerService.register(
                    request.getParameter("account"),
                    request.getParameter("password"),
                    request.getParameter("email")
            );
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
        } catch (DuplicateException e) {
            LOG.info("account 또는 email 중복으로 인한 회원가입 실패");
        }
        return new ModelAndView(new JspView("redirect:/index.jsp"));
    }
}

