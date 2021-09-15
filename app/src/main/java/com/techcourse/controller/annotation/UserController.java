package com.techcourse.controller.annotation;

import static nextstep.web.support.RequestMethod.GET;

import com.techcourse.domain.User;
import com.techcourse.exception.NoSuchUserException;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/api/user", method = GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        final String account = request.getParameter("account");

        LOG.info("user id: {}", account);

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        final User user = InMemoryUserRepository.findByAccount(account)
            .orElseThrow(NoSuchUserException::new);

        modelAndView.addObject("user", user);

        return modelAndView;
    }
}
