package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {

        if (!isValidRequest(request)) {
            return new ModelAndView(new JspView("redirect:/404.jsp"));
        }

        final Optional<User> user = findUser(request);

        if (user.isEmpty()) {
            return new ModelAndView(new JspView("redirect:/404.jsp"));
        }

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user.get());
        return modelAndView;
    }

    private boolean isValidRequest(HttpServletRequest request) {
        return request.getParameter("account") != null;
    }

    private Optional<User> findUser(HttpServletRequest request) {
        final String account = request.getParameter("account");
        log.info("user id : {}", account);

        return InMemoryUserRepository.findByAccount(account);
    }
}
