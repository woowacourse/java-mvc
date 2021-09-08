package com.techcourse.controller;

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
public class RestController {

    private static final Logger log = LoggerFactory.getLogger(RestController.class);


//    @RequestMapping(value = "/" , method = RequestMethod.GET)
//    public String main(HttpServletRequest request, HttpServletResponse response){
//        return "/index.jsp";
//    }
//
//    @RequestMapping(value="/login", method = RequestMethod.POST)
//    public String login(HttpServletRequest request, HttpServletResponse response){
//        if (UserSession.isLoggedIn(request.getSession())) {
//            return "redirect:/index.jsp";
//        }
//
//        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
//                .map(user -> {
//                    log.info("User : {}", user);
//                    return login(request, user);
//                })
//                .orElse("redirect:/401.jsp");
//    }
//
//    @RequestMapping(value = "login", method=RequestMethod.GET)
//    public String showLogin(HttpServletRequest request, HttpServletResponse response){
//        return UserSession.getUserFrom(request.getSession())
//                .map(user -> {
//                    log.info("logged in {}", user.getAccount());
//                    return "redirect:/index.jsp";
//                })
//                .orElse("/login.jsp");
//    }
//
//    @RequestMapping(value = "/logout", method = RequestMethod.GET)
//    public String logout(HttpServletRequest request, HttpServletResponse response){
//        final HttpSession session = request.getSession();
//        session.removeAttribute(UserSession.SESSION_KEY);
//        return "redirect:/";
//    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(HttpServletRequest request, HttpServletResponse response) {
        final User user = new User(2,
                request.getParameter("account"),
                request.getParameter("password"),
                request.getParameter("email"));
        InMemoryUserRepository.save(user);

        JspView jspView = new JspView("redirect:/index.jsp");
        ModelAndView modelAndView = new ModelAndView(jspView);

        return modelAndView;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView showResister(HttpServletRequest request, HttpServletResponse response) {
        JspView jspView = new JspView("/register.jsp");
        ModelAndView modelAndView = new ModelAndView(jspView);

        return modelAndView;
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
}
