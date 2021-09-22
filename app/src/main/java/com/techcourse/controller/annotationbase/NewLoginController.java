package com.techcourse.controller.annotationbase;

import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class NewLoginController {

    private static final Logger log = LoggerFactory.getLogger(NewLoginController.class);

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView viewLogin(HttpServletRequest req, HttpServletResponse res) {
        return UserSession.getUserFrom(req.getSession())
            .map(user -> {
                log.info("logged in {}", user.getAccount());
                return ModelAndView.fromJspView("redirect:/index.jsp");
            })
            .orElse(ModelAndView.fromJspView("/login.jsp"));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest req, HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return ModelAndView.fromJspView("redirect:/index.jsp");
        }

        return InMemoryUserRepository.findByAccount(req.getParameter("account"))
            .map(user -> {
                log.info("User : {}", user);
                return login(req, user);
            })
            .orElse(ModelAndView.fromJspView("redirect:/401.jsp"));
    }

    private ModelAndView login(HttpServletRequest request, User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return ModelAndView.fromJspView("redirect:/index.jsp");
        }
        return ModelAndView.fromJspView("redirect:/401.jsp");
    }
}
