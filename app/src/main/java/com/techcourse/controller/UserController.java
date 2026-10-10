package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
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
    private final JsonView jsonView = new JsonView();

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse response) {
        final String account = request.getParameter("account");
        log.debug("user id : {}", account);

        if (account == null || account.isBlank()) {
            return error(response, HttpServletResponse.SC_BAD_REQUEST, "account parameter is required");
        }

        return InMemoryUserRepository.findByAccount(account)
                .map(this::userResponse)
                .orElseGet(() -> error(response, HttpServletResponse.SC_NOT_FOUND, "user not found"));
    }

    private ModelAndView userResponse(final User user) {
        return new ModelAndView(jsonView).addObject("user", user);
    }

    private ModelAndView error(final HttpServletResponse response, final int status, final String message) {
        response.setStatus(status);
        return new ModelAndView(jsonView).addObject("error", Map.of("message", message));
    }
}
