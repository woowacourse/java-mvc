package com.techcourse.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.annotation.ResponseBody;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @ResponseBody
    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public Map<String, Object> show(final HttpServletRequest request, final Map<String, Object> model) {
        final String account = request.getParameter("account");
        log.debug("user id : {}", account);

        final Map<String, Object> responseData = new HashMap<>();
        findUserByAccount(account).ifPresentOrElse(
                user -> responseData.put("user", user),
                () -> responseData.put("message", "해당 계정의 사용자가 존재하지 않습니다.")
        );

        return responseData;
    }

    private Optional<User> findUserByAccount(final String account) {
        return InMemoryUserRepository.findByAccount(account);
    }
}
