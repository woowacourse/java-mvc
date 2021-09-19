package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.mvc.view.JsonConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonConverterTest {

    @DisplayName("User -> JSON 형식으로 변환한다.")
    @Test
    void toJson() {
        // given
        JsonConverter jsonConverter = new JsonConverter();

        User user = new User(1L, "dani", "dani", "dani@woowahan.com");
        String expected = "{\"id\":1,\"account\":\"dani\",\"password\":\"dani\",\"email\":\"dani@woowahan.com\"}";

        // when
        String actual = jsonConverter.toJson(user);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    private static class User {

        private final Long id;
        private final String account;
        private final String password;
        private final String email;

        public User(Long id, String account, String password, String email) {
            this.id = id;
            this.account = account;
            this.password = password;
            this.email = email;
        }

        public Long getId() {
            return id;
        }

        public String getAccount() {
            return account;
        }

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
        }
    }
}
