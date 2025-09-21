package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class RegisterController {

    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ModelAndView execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final var user = new User(2,
                req.getParameter("account"),
                req.getParameter("password"),
                req.getParameter("email"));
        InMemoryUserRepository.save(user);
        StringBuilder builder = new StringBuilder();
        log.info(builder.append("회원가입 성공 : ").append(user).toString());

        return ModelAndView.ofJspView("redirect:/index.jsp");
    }
}
