package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.mvc.controller.ResponseEntity;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> show(HttpServletRequest request,
        HttpServletResponse response) {
        final String account = request.getParameter("account");
        log.debug("user id : {}", account);

        final User user = InMemoryUserRepository.findByAccount(account)
            .orElseThrow();

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("user", user);
        return new ResponseEntity<>(userInfo);
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> showAll(HttpServletRequest request,
        HttpServletResponse response) {
        Map<String, Object> userInfo = new HashMap<>();
        final List<User> users = InMemoryUserRepository.findAll();
        users.forEach(user -> userInfo.put(user.getAccount(), user));
        return new ResponseEntity<>(userInfo);
    }
}