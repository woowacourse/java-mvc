package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.mvc.exception.BadRequestException;
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
        try {
            final User user = new User(2,
                    req.getParameter("account"),
                    req.getParameter("password"),
                    req.getParameter("email"));
            InMemoryUserRepository.save(user);
        } catch (IllegalStateException e) {
            throw new BadRequestException("회원가입 폼 데이터가 누락 되었습니다.");
        }
    }

    private ModelAndView redirectToIndex() {
        final String viewName = "redirect:/";
        final JspView jspView = new JspView(viewName);
        return new ModelAndView(jspView);
    }
}
