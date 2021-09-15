package com.techcourse.air.controller;

import com.techcourse.air.core.annotation.Controller;
import com.techcourse.air.domain.User;
import com.techcourse.air.mvc.core.view.JsonView;
import com.techcourse.air.mvc.core.view.ModelAndView;
import com.techcourse.air.mvc.web.annotation.RequestMapping;
import com.techcourse.air.mvc.web.annotation.ResponseBody;
import com.techcourse.air.mvc.web.support.RequestMethod;
import com.techcourse.air.repository.InMemoryUserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    // test용 GET /api/user?account=gugu
    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        final String account = request.getParameter("account");
        log.debug("user id : {}", account);

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        final User user = InMemoryUserRepository.findByAccount(account)
                                                .orElseThrow();

        modelAndView.addObject("user", user);
        return modelAndView;
    }

    // test용 GET /api/user/v2?account=gugu
    @RequestMapping(value = "/api/user/v2", method = RequestMethod.GET)
    @ResponseBody
    public User show2(HttpServletRequest request, HttpServletResponse response) {
        final String account = request.getParameter("account");
        log.debug("user id : {}", account);

        return InMemoryUserRepository.findByAccount(account)
                                     .orElseThrow();
    }

    // test용 GET /api/ok
    @RequestMapping(value = "/api/ok", method = RequestMethod.GET)
    @ResponseBody
    public String ok(HttpServletRequest request, HttpServletResponse response) {
        return "ok";
    }
}
