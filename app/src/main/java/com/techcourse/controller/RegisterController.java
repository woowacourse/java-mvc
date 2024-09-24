package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

public class RegisterController implements Controller {

    private static final String ACCOUNT = "account";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String REDIRECT_INDEX_JSP = "redirect:/index.jsp";
    private static final String REDIRECT_404_JSP = "redirect:/404.jsp";

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        if (req.getParameter(ACCOUNT) == null ||
                req.getParameter(PASSWORD) == null || req.getParameter(EMAIL) == null) {
            return REDIRECT_404_JSP;
        }

        final var user = new User(2,
                req.getParameter(ACCOUNT),
                req.getParameter(PASSWORD),
                req.getParameter(EMAIL));
        InMemoryUserRepository.save(user);

        return REDIRECT_INDEX_JSP;
    }
}
