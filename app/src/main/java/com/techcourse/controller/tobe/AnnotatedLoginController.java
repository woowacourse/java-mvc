package com.techcourse.controller.tobe;

import com.techcourse.controller.UserSession;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AnnotatedLoginController {

    private static final Logger log = LoggerFactory.getLogger(AnnotatedLoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest req, HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            final User user = UserSession.getUserFrom(req.getSession()).orElseThrow();
            final ModelAndView modelAndView = new ModelAndView(new JspView("redirect:/index.jsp"));
            modelAndView.addObject("account", user.getAccount());
            return modelAndView;
        }

        return InMemoryUserRepository.findByAccount(req.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(req, user);
                })
                .orElse(new ModelAndView(new JspView("redirect:/401.jsp")));
    }

    private ModelAndView login(HttpServletRequest request, User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            final ModelAndView modelAndView = new ModelAndView(new JspView("redirect:/index.jsp"));
            modelAndView.addObject("account", user.getAccount());
            return modelAndView;
        } else {
            return new ModelAndView(new JspView("redirect:/401.jsp"));
        }
    }

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req, HttpServletResponse res) {
        return UserSession.getUserFrom(req.getSession()).map(user -> {
            log.info("logged in {}", user.getAccount());
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        })
                .orElse(new ModelAndView(new JspView("/login.jsp")));
    }
}
