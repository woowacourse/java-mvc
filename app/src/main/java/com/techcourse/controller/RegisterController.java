package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.exception.BadRequestException;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(final HttpServletRequest req, final HttpServletResponse res)
            throws BadRequestException {
        saveUser(req);

        return redirectToIndex();
    }

    private void saveUser(final HttpServletRequest req) throws BadRequestException {
        final String account = req.getParameter("account");
        final String password = req.getParameter("password");
        final String email = req.getParameter("email");
        if (account == null || password == null || email == null) {
            throw new BadRequestException("회원가입 폼 데이터가 누락 되었습니다.");
        }

        final User user = new User(2, account, password, email);
        InMemoryUserRepository.save(user);
    }

    private ModelAndView redirectToIndex() {
        final String viewName = "redirect:/";
        final JspView jspView = new JspView(viewName);
        return new ModelAndView(jspView);
    }
}
