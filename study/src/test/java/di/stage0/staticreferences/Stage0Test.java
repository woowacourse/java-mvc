package di.stage0.staticreferences;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import di.User;

class Stage0Test {

    /**
     * UserService 클래스의 join 메서드는 정적 메서드다. 객체지향적이지 않은 코드는 변경에 취약하다. UserService는 UserDao와 밀접하게 결합되어 있는데 이는 테스트하기 어렵게
     * 만든다. 테스트 DB를 사용하려면 코드의 많은 부분이 수정되어야 한다.
     */
    @Test
    void stage0() {
        final var user = new User(1L, "gugu");

        final UserDao userDao = new UserDao();
        final UserService userService = new UserService(userDao);

        final var actual = userService.join(user);

        assertThat(actual.getAccount()).isEqualTo("gugu");
    }
}
