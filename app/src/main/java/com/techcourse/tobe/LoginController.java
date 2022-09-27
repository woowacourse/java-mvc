package com.techcourse.tobe;

import static com.techcourse.tobe.UserSession.SESSION_KEY;

import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(com.techcourse.controller.LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        if (UserSession.isLoggedIn(req.getSession())) {
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }

        return InMemoryUserRepository.findByAccount(req.getParameter("account"))
                .map(user -> checkLogin(req, user))
                .orElse(new ModelAndView(new JspView("redirect:/401.jsp")));
    }

    private ModelAndView checkLogin(final HttpServletRequest request, final User user) {
        log.info("User : {}", user);

        if (!user.checkPassword(request.getParameter("password"))) {
            return new ModelAndView(new JspView("redirect:/401.jsp"));
        }

        final JspView view = new JspView("/index.jsp");
        final ModelAndView modelAndView = new ModelAndView(view);
        modelAndView.addObject(SESSION_KEY, user);
        return modelAndView;
    }

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView showLogin(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final Optional<User> user = UserSession.getUserFrom(req.getSession());

        if (user.isPresent()) {
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }
        return new ModelAndView(new JspView("/login.jsp"));
    }
}
