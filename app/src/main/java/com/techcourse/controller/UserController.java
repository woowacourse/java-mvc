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
import java.util.Optional;

@Controller
public class UserController {

  @RequestMapping(value = "/api/user", method = RequestMethod.GET)
  public ModelAndView show(
      final HttpServletRequest request, final HttpServletResponse response) {
    final ModelAndView modelAndView = new ModelAndView(new JsonView());
    final String account = request.getParameter("account");
    if (account == null || account.isBlank()) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return modelAndView.addObject("error", Map.of("message", "account is required"));
    }

    final Optional<User> user = InMemoryUserRepository.findByAccount(account);
    if (user.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return modelAndView.addObject("error", Map.of("message", "User not found"));
    }

    return modelAndView.addObject("user", user.get());
  }
}
