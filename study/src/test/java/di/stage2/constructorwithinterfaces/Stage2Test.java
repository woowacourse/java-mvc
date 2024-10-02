package di.stage2.constructorwithinterfaces;

import di.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Stage2Test {

    /**
     * 인터페이스를 사용하여 결합도를 낮췄다.
     * 인터페이스를 사용하면 testAnonymousClass 테스트 케이스처럼 개발자가 구현 객체를 바꿔서 사용할 수 있다.
     * 하지만 아직도 문제가 있다.
     * 인터페이스의 구현 객체를 누군가가 결정해줘야 한다.
     * 객체를 생성하고 연결해주는 역할이 필요하다.
     */
    @Test
    void stage2() {
        final var user = new User(1L, "gugu");

        /**
         * 애플리케이션 코드에 구체 클래스를 결정하고, 의존성을 주입하는 코드를 계속 작성해야한다.
         * 의존성을 변경하고 싶다면 결국 기존 애플리케이션 코드의 수정이 불가피하다는 문제가 존재한다.
         * stage2만 놓고 본다면 여전히 DIP와 OCP를 위반하고 있다. (켈리 생각 ㅎ
         */
        final UserDao userDao = new InMemoryUserDao();
        final var userService = new UserService(userDao);

        final var actual = userService.join(user);

        assertThat(actual.getAccount()).isEqualTo("gugu");
    }

    @Test
    void testAnonymousClass() {
        // given
        final var userDao = new UserDao() {
            private User user;

            @Override
            public void insert(User user) {
                this.user = user;
            }

            @Override
            public User findById(long id) {
                return user;
            }
        };
        final var userService = new UserService(userDao);
        final var user = new User(1L, "gugu");

        // when
        final var actual = userService.join(user);

        // then
        assertThat(actual.getAccount()).isEqualTo("gugu");
    }
}
