package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.Account;
import com.techcourse.domain.Email;
import com.techcourse.domain.Password;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class RegisterController {

    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return UserSession.getUserFrom(request.getSession())
                .map(user -> {
                    log.info("logged in {}", user);
                    return new ModelAndView(new JspView("redirect:/index.jsp"));
                })
                .orElse(new ModelAndView(new JspView("/register.jsp")));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final Account account = new Account(req.getParameter("account"));
        final Password password = new Password(req.getParameter("password"));
        final Email email = new Email(req.getParameter("email"));
        final User user = new User(account, password, email);
        InMemoryUserRepository.save(user);

        log.info("Registered in {}", user);
        return new ModelAndView(new JspView("redirect:/login.jsp"));
    }
}
