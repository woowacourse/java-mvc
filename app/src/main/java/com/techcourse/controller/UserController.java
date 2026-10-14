package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
import com.techcourse.controller.dto.ErrorResponse;
import com.techcourse.controller.dto.UserResponse;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserController {

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse response) {
        final String account = request.getParameter("account");
        if (account == null || account.isBlank()) {
            return error(response, HttpServletResponse.SC_BAD_REQUEST,
                    "INVALID_ACCOUNT", "account is required");
        }

        return InMemoryUserRepository.findByAccount(account)
                .map(user -> new ModelAndView(new JsonView())
                        .addObject("user", new UserResponse(user.getAccount())))
                .orElseGet(() -> error(response, HttpServletResponse.SC_NOT_FOUND,
                        "USER_NOT_FOUND", "user not found"));
    }

    private ModelAndView error(final HttpServletResponse response, final int status,
                               final String code, final String message) {
        response.setStatus(status);
        return new ModelAndView(new JsonView()).addObject("error", new ErrorResponse(code, message));
    }
}
