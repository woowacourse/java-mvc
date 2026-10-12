package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
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
    public ModelAndView show(final HttpServletRequest req, final HttpServletResponse res) {
        final String account = req.getParameter("account");
        log.debug("user id : {}", account);

        if (account == null || account.isBlank()) {
            return error(res, HttpServletResponse.SC_BAD_REQUEST, "account는 필수입니다.");
        }

        final var user = InMemoryUserRepository.findByAccount(account);
        if (user.isEmpty()) {
            return error(res, HttpServletResponse.SC_NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }

        final var modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user.get());

        return modelAndView;
    }

    private ModelAndView error(final HttpServletResponse response, final int status, final String message) {
        response.setStatus(status);
        return new ModelAndView(new JsonView())
                .addObject("error", Map.of("message", message));
    }
}
