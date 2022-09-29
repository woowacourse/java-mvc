package com.techcourse.controller;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView showPage(final HttpServletRequest request, final HttpServletResponse response) {
        return ifUserLoggedIn(request, Page.LOGIN::getModelAndView);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest request, final HttpServletResponse response) {
        return ifUserLoggedIn(request, () -> {
            final var account = request.getParameter("account");
            final var password = request.getParameter("password");

            if (isNullAnyParameter(account, password)) {
                return Page.ERROR_404.getModelAndView();
            }

            final Optional<User> user = InMemoryUserRepository.findByAccount(account)
                    .filter(user1 -> user1.checkPassword(password));

            if (user.isEmpty()) {
                return Page.ERROR_401.getRedirectModelAndView();
            }

            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return Page.INDEX.getRedirectModelAndView();
        });
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(final HttpServletRequest req, final HttpServletResponse res) {
        final var session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return new ModelAndView(new JspView("redirect:/"));
    }

    private boolean isNullAnyParameter(final String... parameters) {
        return Stream.of(parameters)
                .anyMatch(Objects::nonNull);
    }

    private ModelAndView ifUserLoggedIn(final HttpServletRequest request,
                                        final Supplier<ModelAndView> supplier) {
        final HttpSession session = request.getSession();
        if (UserSession.isLoggedIn(session)) {
            return Page.INDEX.getRedirectModelAndView();
        }
        return supplier.get();
    }
}
