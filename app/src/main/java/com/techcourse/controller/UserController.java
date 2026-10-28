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

import java.util.Optional;

@Controller
public class UserController {

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String account = request.getParameter("account");
        if (account == null) {
            return errorResponse(HttpServletResponse.SC_BAD_REQUEST);
        }

        Optional<User> user = InMemoryUserRepository.findByAccount(account);
        if (user.isEmpty()) {
            return errorResponse(HttpServletResponse.SC_NOT_FOUND);
        }

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user.orElseThrow());
        return modelAndView;
    }

    private ModelAndView errorResponse(int status) {
        return new ModelAndView((model, request, response) -> response.sendError(status));
    }
}
