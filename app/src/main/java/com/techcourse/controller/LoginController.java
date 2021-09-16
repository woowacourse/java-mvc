package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.exception.UnAuthorizedException;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req, HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }
        return new ModelAndView(new JspView("/login.jsp"));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest req, HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }

        String account = req.getParameter("account");
        String password = req.getParameter("password");

        try {
            log.info("로그인 요청 => account : {}, password : {}", account, password);
            setUserToSession(req, account, password);
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        } catch (UnAuthorizedException e) {
            return new ModelAndView(new JspView("redirect:/401.jsp"));
        }
    }

    private void setUserToSession(HttpServletRequest req, String account, String password) {
        User foundUser = InMemoryUserRepository.findByAccount(account)
                .orElseThrow(() -> new UnAuthorizedException("해당 계정이 존재하지 않습니다."));
        if (!foundUser.checkPassword(password)) {
            throw new UnAuthorizedException("비밀번호가 일치하지 않습니다.");
        }
        HttpSession session = req.getSession();
        session.setAttribute(UserSession.SESSION_KEY, foundUser);
    }
}
