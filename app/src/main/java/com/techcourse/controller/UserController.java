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
        final var modelAndView = new ModelAndView(new JsonView());
        final String account = request.getParameter("account");
        if (account == null || account.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            modelAndView.addObject("error", Map.of("message", "계정이 필요합니다."));
            return modelAndView;
        }
        final var user = InMemoryUserRepository.findByAccount(account);
        if (user.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            modelAndView.addObject("error", Map.of("message", "사용자를 찾을 수 없습니다."));
            return modelAndView;
        }
        modelAndView.addObject("user", Map.of("account", user.get().getAccount()));
        return modelAndView;
    }
}
