package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public Map<String, Object> show(HttpServletRequest request, HttpServletResponse response) {
        final String account = request.getParameter("account");
        log.debug("user id : {}", account);

        final User user = InMemoryUserRepository.findByAccount(account)
                .orElseThrow();

        return Map.of("user", user);
    }
}
