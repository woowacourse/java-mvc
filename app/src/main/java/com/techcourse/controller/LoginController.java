package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

  private static final Logger log = LoggerFactory.getLogger(LoginController.class);

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public ModelAndView login(
      final HttpServletRequest request, final HttpServletResponse response) {
    if (UserSession.isLoggedIn(request.getSession())) {
      return new ModelAndView(new JspView("redirect:/index.jsp"));
    }

    final String account = request.getParameter("account");
    if (account == null) {
      return new ModelAndView(new JspView("redirect:/401.jsp"));
    }

    final Optional<User> user = InMemoryUserRepository.findByAccount(account);
    if (user.isEmpty() || !user.get().checkPassword(request.getParameter("password"))) {
      return new ModelAndView(new JspView("redirect:/401.jsp"));
    }

    request.getSession().setAttribute(UserSession.SESSION_KEY, user.get());
    log.info("Logged in account : {}", user.get().getAccount());
    return new ModelAndView(new JspView("redirect:/index.jsp"));
  }
}
