package study;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class JacksonTest {

    static class User {
        private final long id;
        private final String account;

        public User(final long id, final String account) {
            this.id = id;
            this.account = account;
        }

        public long getId() {
            return id;
        }

        public String getAccount() {
            return account;
        }
    }

    @Test
    void writeValueAsString() throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Map<String, User> user = Map.of("user", new User(1L, "hello"));
        final String target = objectMapper.writeValueAsString(user);

        System.out.println(target);
        Assertions.assertThat(target).contains("hello", "1");
    }
}
