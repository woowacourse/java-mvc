package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.stream.Stream;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AnnotationController {

    private static final Logger log = LoggerFactory.getLogger(AnnotationController.class);

    @RequestMapping(value = {"/@mvc", "/@mvc/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView index(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/index.jsp"));
    }

    @RequestMapping(value = "/@mvc/login", method = RequestMethod.GET)
    public ModelAndView login(final HttpServletRequest request, final HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }

        final var account = request.getParameter("account");
        if (Objects.isNull(account)) {
            return new ModelAndView(new JspView("redirect:/401.jsp"));
        }

        final var viewName = InMemoryUserRepository.findByAccount(account)
                .map(user -> {
                    log.info("User : {}", user);
                    return login(request, user);
                })
                .orElse("redirect:/401.jsp");

        return new ModelAndView(new JspView(viewName));
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

    @RequestMapping(value = "/@mvc/login/view", method = RequestMethod.GET)
    public ModelAndView loginView(final HttpServletRequest request, final HttpServletResponse response) {
        final var viewName = UserSession.getUserFrom(request.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return "redirect:/index.jsp";
                })
                .orElse("/login.jsp");

        return new ModelAndView(new JspView(viewName));
    }

    @RequestMapping(value = "/@mvc/logout", method = RequestMethod.GET)
    public ModelAndView logout(final HttpServletRequest request, final HttpServletResponse response) {
        final var session = request.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);

        return new ModelAndView(new JspView("redirect:/"));
    }

    @RequestMapping(value = "/@mvc/register/view", method = RequestMethod.GET)
    public ModelAndView registerView(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/register.jsp"));
    }

    @RequestMapping(value = "/@mvc/register", method = RequestMethod.GET)
    public ModelAndView register(final HttpServletRequest request, final HttpServletResponse response) {
        if (absenceEssential(request)) {
            return new ModelAndView(new JspView("redirect:/register.jsp"));
        }
        registerUser(request);

        return new ModelAndView(new JspView("redirect:/index.jsp"));
    }

    private boolean absenceEssential(final HttpServletRequest req) {
        final var account = req.getParameter("account");
        final var password = req.getParameter("password");
        final var email = req.getParameter("email");

        return Stream.of(account, password, email)
                .anyMatch(Objects::isNull);
    }

    private void registerUser(final HttpServletRequest request) {
        final var user = new User(
                request.getParameter("account"),
                request.getParameter("password"),
                request.getParameter("email")
        );

        InMemoryUserRepository.save(user);
    }
}
