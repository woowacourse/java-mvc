package nextstep.context.test_case_2;

import nextstep.web.annotation.GiveMePeanut;
import nextstep.web.annotation.Service;

@Service
public class UserService {

    @GiveMePeanut
    private UserDao userDao;

    public User join(final User user) {
        userDao.save(user);
        return userDao.findByAccount(user.getAccount());
    }

    private UserService() {}
}
