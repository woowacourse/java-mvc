package com.techcourse.controller.v2;

import static nextstep.web.support.RequestMethod.GET;

import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginV2Controller {

    private static final Logger log = LoggerFactory.getLogger(LoginV2Controller.class);

    private InMemoryUserRepository userRepository;

    public LoginV2Controller(final InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/v2/login/view", method = GET)
    public ModelAndView loginView(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final String viewName = UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return "redirect:/index.jsp";
                })
                .orElse("/login.jsp");
        return new ModelAndView(new JspView(viewName));
    }

    @RequestMapping(value = "/v2/login", method = GET)
    public ModelAndView login(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        if (UserSession.isLoggedIn(req.getSession())) {
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }

        String viewName = findByAccount(req);
        return new ModelAndView(new JspView(viewName));
    }

    @RequestMapping(value = "/v2/logout", method = GET)
    public ModelAndView logout(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final var session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return new ModelAndView(new JspView("redirect:/"));
    }

    private String findByAccount(final HttpServletRequest req) {
        return userRepository.findByAccount(req.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(req, user);
                })
                .orElse("redirect:/401.jsp");
    }

    private String login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return "redirect:/index.jsp";
        } else {
            return "redirect:/401.jsp";
        }
    }
}
