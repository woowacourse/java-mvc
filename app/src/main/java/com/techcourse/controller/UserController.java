package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.exception.ApplicationException;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView showSingleUser(HttpServletRequest request, HttpServletResponse response) {
        final String account = request.getParameter("account");
        log.debug("user id : {}", account);

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        final User user = InMemoryUserRepository.findByAccount(account)
                .orElseThrow(() -> new ApplicationException("사용자를 찾을 수 없습니다."));

        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/api/all-users", method = RequestMethod.GET)
    public ModelAndView showAllUsers(HttpServletRequest request, HttpServletResponse response) {
        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        final Map<String, User> allUsers = InMemoryUserRepository.findAllUsers();
        for (Map.Entry<String, User> userEntry : allUsers.entrySet()) {
            modelAndView.addObject(userEntry.getKey(), userEntry.getValue());
        }
        return modelAndView;
    }

    @RequestMapping(value = "/api/two-users", method = RequestMethod.GET)
    public ModelAndView showTwoUsers(HttpServletRequest request, HttpServletResponse response) {
        final String account1 = request.getParameter("account1");
        final String account2 = request.getParameter("account2");
        log.debug("user id : {}", account1);
        log.debug("user id : {}", account2);

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        final User user1 = InMemoryUserRepository.findByAccount(account1)
                .orElseThrow(() -> new ApplicationException("사용자를 찾을 수 없습니다."));
        final User user2 = InMemoryUserRepository.findByAccount(account2)
                .orElseThrow(() -> new ApplicationException("사용자를 찾을 수 없습니다."));

        modelAndView.addObject("user1", user1);
        modelAndView.addObject("user2", user2);
        return modelAndView;
    }
}
