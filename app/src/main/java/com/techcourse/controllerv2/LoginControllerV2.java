package com.techcourse.controllerv2;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

import static com.techcourse.controllerv2.UserSessionV2.SESSION_KEY;
import static com.techcourse.controllerv2.UserSessionV2.isLoggedIn;
import static web.org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class LoginControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(LoginControllerV2.class);

    @RequestMapping(value = "/login", method = POST)
    public ModelAndView execute(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return new ModelAndView(new JspView(getViewName(request)));
    }

    private String getViewName(final HttpServletRequest request) {
        if (isLoggedIn(request.getSession())) {
            return "redirect:/index.jsp";
        }

        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
                .map(user -> {
                    log.info("==========> User = {}", user);
                    return login(request, user);
                })
                .orElse("redirect:/401.jsp");
    }

    private String login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final HttpSession session = request.getSession();
            session.setAttribute(SESSION_KEY, user);
            return "redirect:/index.jsp";
        }
        return "redirect:/401.jsp";
    }
}
