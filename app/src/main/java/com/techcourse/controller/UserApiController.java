package com.techcourse.controller;

import com.exception.NotFoundException;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserApiController {

    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        final String account = request.getParameter("account");
        log.debug("user id: {}", account);
        final User user = InMemoryUserRepository.findByAccount(account)
                .orElseThrow(() -> new NotFoundException("찾을 수 없는 사용자 입니다. 입력값: " + account));
        modelAndView.addObject(account, user);
        return modelAndView;
    }
}
