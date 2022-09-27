package com.techcourse.controller.v2.controller;

import static nextstep.web.support.RequestMethod.GET;

import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;

@Controller
public class HomeController {

    private InMemoryUserRepository inMemoryUserRepository;

    public HomeController(final InMemoryUserRepository inMemoryUserRepository) {
        this.inMemoryUserRepository = inMemoryUserRepository;
    }

    @RequestMapping(value = "/", method = GET)
    public ModelAndView index(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return new ModelAndView(new JspView("/index.jsp"));
    }
}
