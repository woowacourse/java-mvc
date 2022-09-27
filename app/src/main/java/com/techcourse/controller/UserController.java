package com.techcourse.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse response) {
        final String account = request.getParameter("account");

        if (account == null) {
            return generateErrorModelAndView("acoount 정보가 없습니다.");
        }

        log.debug("user id : {}", account);

        final Optional<User> user = InMemoryUserRepository.findByAccount(account);
        if (user.isEmpty()) {
            return generateErrorModelAndView("존재하지 않는 사용자입니다.");
        }

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user.get());

        return modelAndView;
    }

    private ModelAndView generateErrorModelAndView(String errorMessage) {
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("error", errorMessage);
        return modelAndView;
    }
}
