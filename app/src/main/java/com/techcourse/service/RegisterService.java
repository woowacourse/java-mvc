package com.techcourse.service;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import com.techcourse.service.dto.RegisterDto;

public class RegisterService {

    public User join(RegisterDto registerDto) {
        final User user = new User(registerDto.getAccount(),
                                   registerDto.getPassword(),
                                   registerDto.getEmail());
        return InMemoryUserRepository.save(user);
    }
}
