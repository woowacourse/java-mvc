package com.techcourse.service;

import com.techcourse.Pages;
import com.techcourse.domain.User;
import com.techcourse.domain.UserSession;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import nextstep.web.annotation.Service;

@Service
public class UserService {

    public User findById(String account) {
        return InMemoryUserRepository.findByAccount(account)
            .orElseThrow();
    }

    public Pages checkedLogin(HttpServletRequest request) {
        User user = findById(request.getParameter("account"));
        if (user.checkPassword(request.getParameter("password"))) {
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return Pages.INDEX;
        }
        return Pages.UNAUTHORIZED;
    }

    public void save(User user) {
        InMemoryUserRepository.save(user);
    }
}
