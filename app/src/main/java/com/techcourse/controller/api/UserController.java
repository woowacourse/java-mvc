package com.techcourse.controller.api;

import com.techcourse.domain.User;
import com.techcourse.exception.NotFoundException;
import com.techcourse.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.techcourse.view.ViewName.INDEX_JSP_VIEW_NAME;
import static com.techcourse.view.ViewName.REDIRECT_PREFIX;

@Controller
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        final String account = request.getParameter("account");
        LOG.debug("user id : {}", account);

        User user;
        try {
            user = userService.findByAccount(account);
        } catch (NotFoundException e) {
            LOG.info("회원 조회 실패 : account에 대항하는 User가 존재하지 않음. 입력 account: {}", account);
            return new ModelAndView(new JspView(REDIRECT_PREFIX + INDEX_JSP_VIEW_NAME));
        }
        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}
