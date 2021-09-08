package com.techcourse.service;

import com.techcourse.domain.User;
import com.techcourse.exception.DuplicateException;
import com.techcourse.repository.InMemoryUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterService {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterService.class);

    public User register(String account, String password, String email) {
        final User user = new User(account, password, email);
        validateNotDuplicate(user);
        return InMemoryUserRepository.save(user);
    }

    private void validateNotDuplicate(User user) {
        validateAccountNotDuplicate(user.getAccount());
        validateEmailNotDuplicate(user.getEmail());
    }

    private void validateAccountNotDuplicate(String account) {
        if (InMemoryUserRepository.existsByAccount(account)) {
            LOG.debug("회원 등록 실패 : account 중복 => account: {}", account);
            throw new DuplicateException("이미 존재하는 account 입니다.");
        }
    }

    private void validateEmailNotDuplicate(String email) {
        if (InMemoryUserRepository.existsByEmail(email)) {
            LOG.debug("회원 등록 실패 : email 중복 => email: {}", email);
            throw new DuplicateException("이미 존재하는 email 입니다.");
        }
    }
}
