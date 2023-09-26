package di.stage1.constructorinjection;

import di.User;

class UserService {

    private final UserRepository userRepository;

    UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User join(User user) {
        userRepository.insert(user);
        return userRepository.findById(user.getId());
    }
}
