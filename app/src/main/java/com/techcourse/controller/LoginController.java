package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.BadRequestException;
import com.techcourse.UnauthorizedException;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest req, final HttpServletResponse res)
            throws BadRequestException, UnauthorizedException {
        if (UserSession.isLoggedIn(req.getSession())) {
            return redirectToIndex();
        }

        return processLogin(req);
    }

    private ModelAndView redirectToIndex() {
        final String viewName = "redirect:/index.jsp";
        final JspView jspView = new JspView(viewName);
        return new ModelAndView(jspView);
    }

    private ModelAndView processLogin(final HttpServletRequest req) throws BadRequestException, UnauthorizedException {
        final String account = req.getParameter("account");
        final String password = req.getParameter("password");
        final User user = authenticate(account, password);

        final HttpSession session = req.getSession();
        session.setAttribute(UserSession.SESSION_KEY, user);
        return redirectToIndex();
    }

    private User authenticate(final String account, final String password)
            throws BadRequestException, UnauthorizedException {
        if (account == null || password == null) {
            throw new BadRequestException("account 또는 password가 입력되지 않았습니다.");
        }

        final Optional<User> userOptional = InMemoryUserRepository.findByAccount(account);
        if (userOptional.isEmpty()) {
            throw new UnauthorizedException("user를 찾을 수 없습니다.");
        }

        final User user = userOptional.get();
        if (!user.checkPassword(password)) {
            throw new UnauthorizedException("account 또는 password가 잘못 되었습니다.");
        }
        return user;
    }
}
