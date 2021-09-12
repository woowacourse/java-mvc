package com.techcourse.controller.annotation;

import static nextstep.web.support.RequestMethod.GET;
import static nextstep.web.support.RequestMethod.POST;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AnnotationRegisterController {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationRegisterController.class);
    private static final String HOME_PATH = "/index";

    @RequestMapping(value = "/register", method = GET)
    public ModelAndView getRegister(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("Annotation Register Controller - GET Method");
        return new ModelAndView(new JspView(request.getRequestURI()));
    }

    @RequestMapping(value = "/register", method = POST)
    public ModelAndView postRegister(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("Annotation Register Controller - POST Method");

        User user = new User(
            2L,
            request.getParameter("account"),
            request.getParameter("password"),
            request.getParameter("email")
        );
        InMemoryUserRepository.save(user);

        return new ModelAndView(new JspView(HOME_PATH));
    }
}
