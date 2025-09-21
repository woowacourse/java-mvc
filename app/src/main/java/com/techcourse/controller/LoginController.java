package com.techcourse.controller;

import com.interface21.webmvc.servlet.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.view.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController{

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        if (UserSession.isLoggedIn(req.getSession())) {
            log.info("로그인 완료된 상태입니다.");
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }
        User user = InMemoryUserRepository.findByAccount(req.getParameter("password"))
                .orElse(null);
        return login(req,user);
    }

    private ModelAndView login(final HttpServletRequest request, final User user) {
        if (user != null && user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            log.info("로그인 성공");
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }
        log.info("로그인 실패");
        return new ModelAndView(new JspView("redirect:/401.jsp"));
    }
}
