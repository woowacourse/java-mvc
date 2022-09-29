package nextstep.context.test_case_2;

import java.util.Optional;

public interface UserDao {

    void save(User user);

    User findByAccount(String account);
}
