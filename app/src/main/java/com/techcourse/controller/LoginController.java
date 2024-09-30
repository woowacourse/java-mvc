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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private static final String INDEX_PATH = "redirect:/index.jsp";
    private static final String UNAUTHORIZED_PATH = "redirect:/401.jsp";

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (UserSession.isLoggedIn(request.getSession())) {
            return redirect(INDEX_PATH);
        }

        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(request, user);
                })
                .orElse(redirect(UNAUTHORIZED_PATH));
    }

    private ModelAndView login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return redirect(INDEX_PATH);
        }
        return redirect(UNAUTHORIZED_PATH);
    }

    private ModelAndView redirect(String path) {
        return new ModelAndView(new JspView(path));
    }
}
