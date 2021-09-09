package com.techcourse.controller;

//public class RegisterController implements Controller {
//
//    @Override
//    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
//        final User user = new User(2,
//                req.getParameter("account"),
//                req.getParameter("password"),
//                req.getParameter("email"));
//        InMemoryUserRepository.save(user);
//
//        return "redirect:/index.jsp";
//    }
//}

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class RegisterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView save(final HttpServletRequest req, final HttpServletResponse res) {
        User user = new User(2,
            req.getParameter("account"),
            req.getParameter("password"),
            req.getParameter("email"));
        InMemoryUserRepository.save(user);

        return new ModelAndView(new JspView("redirect:/index.jsp"));
    }

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest req, final HttpServletResponse res) {
        return new ModelAndView(new JspView("/register.jsp"));
    }
}