package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.exception.BadRequestException;
import com.techcourse.service.RegisterService;
import com.techcourse.service.dto.RegisterDto;
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

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
        log.info("Method: POST, Request URI: {}", request.getRequestURI());

        try {
            final User user = registerService.join(RegisterDto.of(request.getParameter("account"),
                                                                  request.getParameter("password"),
                                                                  request.getParameter("email")));
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
        } catch (BadRequestException e) {
            return new ModelAndView(new JspView("/400.jsp"));
        }

        return new ModelAndView(new JspView("redirect:/"));
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        log.info("Method: GET, Request URI: {}", request.getRequestURI());

        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView("redirect:/"));
        }

        return new ModelAndView(new JspView("/register.jsp"));
    }
}
