package com.techcourse.service;

import com.techcourse.domain.User;
import com.techcourse.exception.JoinFailedException;
import com.techcourse.repository.InMemoryUserRepository;
import com.techcourse.service.dto.RegisterDto;
import java.util.Optional;

public class RegisterService {

    public User join(RegisterDto registerDto) {
        final String account = registerDto.getAccount();
        checkDuplicateAccount(account);

        final User user = new User(account,
                                   registerDto.getPassword(),
                                   registerDto.getEmail());
        return InMemoryUserRepository.save(user);
    }

    private void checkDuplicateAccount(String account) {
        final Optional<User> findUser = InMemoryUserRepository.findByAccount(account);
        if (findUser.isPresent()) {
            throw new JoinFailedException();
        }
    }
}
