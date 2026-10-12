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

@Controller
public class UserController {

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse response) {
        final String account = request.getParameter("account");
        if (account == null || account.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new ModelAndView(new JsonView())
                    .addObject("error", Map.of("message", "account는 필수입니다."));
        }

        final var foundUser = InMemoryUserRepository.findByAccount(account);
        if (foundUser.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return new ModelAndView(new JsonView())
                    .addObject("error", Map.of("message", "사용자를 찾을 수 없습니다."));
        }

        final var modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", foundUser.get());
        return modelAndView;
    }
}
