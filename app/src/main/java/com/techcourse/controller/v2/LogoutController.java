package com.techcourse.controller.v2;

import com.techcourse.controller.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@nextstep.web.annotation.Controller
public class LogoutController {

    @RequestMapping(value = "/v2/logout", method = RequestMethod.POST)
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final var session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return "redirect:/";
    }
}
