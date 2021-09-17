package com.techcourse.controller.tobe;

import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView homePage(HttpServletRequest req, HttpServletResponse res) {
        return new ModelAndView(new JspView("index.jsp"));
    }

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView loginPage(HttpServletRequest req, HttpServletResponse res) {
        String page = UserSession.getUserFrom(req.getSession())
                                 .map(user -> {
                                     log.info("logged in {}", user.getAccount());
                                     return "redirect:/index.jsp";
                                 })
                                 .orElse("/login.jsp");
        return new ModelAndView(new JspView(page));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView loginMapping(HttpServletRequest req, HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }

        String account = InMemoryUserRepository.findByAccount(req.getParameter("account"))
                                               .map(user -> {
                                                   log.info("User : {}", user);
                                                   return login(req, user);
                                               })
                                               .orElse("redirect:/401.jsp");
        return new ModelAndView(new JspView(account));
    }

    private String login(HttpServletRequest request, User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return "redirect:/index.jsp";
        } else {
            return "redirect:/401.jsp";
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest req, HttpServletResponse res) {
        final HttpSession session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return new ModelAndView(new JspView("redirect:/index.jsp"));
    }

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView registerPage(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView("/register.jsp"));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(HttpServletRequest req, HttpServletResponse res) {
        final User user = new User(2,
                req.getParameter("account"),
                req.getParameter("password"),
                req.getParameter("email"));
        InMemoryUserRepository.save(user);

        return new ModelAndView(new JspView("redirect:/index.jsp"));
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        final String account = request.getParameter("account");
        log.debug("user id : {}", account);

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        final User user = InMemoryUserRepository.findByAccount(account)
                                                .orElseThrow();

        modelAndView.addObject("user", user);
        return modelAndView;
    }
}
