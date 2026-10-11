package json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JacksonTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("객체의 getter로 JSON 속성을 찾으며 toString은 사용하지 않는다")
    void serializeObject() throws Exception {
        final var user = new SampleUser("마이찬", "secret");

        final String json = objectMapper.writeValueAsString(user);

        assertThat(json).isEqualTo("{\"account\":\"마이찬\"}");
    }

    @Test
    @DisplayName("기본 설정에서 직렬화할 속성이 없는 객체는 예외가 발생한다")
    void serializeObjectWithoutProperties() {
        assertThatThrownBy(() -> objectMapper.writeValueAsString(new HiddenUser()))
                .isInstanceOf(InvalidDefinitionException.class);
    }

    @Test
    @DisplayName("Map은 JSON 객체로, List는 JSON 배열로 변환한다")
    void serializeMapAndList() throws Exception {
        final var model = Map.of("users", List.of(new SampleUser("마이찬", "secret")), "count", 1);

        final String json = objectMapper.writeValueAsString(model);

        // JSON 객체의 키 순서 대신 구조와 값을 비교한다.
        assertThat(objectMapper.readTree(json)).isEqualTo(objectMapper.readTree("""
                {"users": [{"account": "마이찬"}], "count": 1}
                """));
    }

    @Test
    @DisplayName("항목이 하나인 Map도 Jackson은 키를 유지한다")
    void singleEntryMapKeepsKey() throws Exception {
        final var user = new SampleUser("마이찬", "secret");

        final String modelJson = objectMapper.writeValueAsString(Map.of("user", user));
        final String valueJson = objectMapper.writeValueAsString(user);

        assertThat(objectMapper.readTree(modelJson)).isEqualTo(objectMapper.readTree("""
                {"user": {"account": "마이찬"}}
                """));
        assertThat(objectMapper.readTree(valueJson)).isEqualTo(objectMapper.readTree("""
                {"account": "마이찬"}
                """));
    }

    @Test
    @DisplayName("null과 빈 Map은 각각 JSON null과 빈 객체로 변환한다")
    void serializeNullAndEmptyMap() throws Exception {
        assertThat(objectMapper.writeValueAsString(null)).isEqualTo("null");
        assertThat(objectMapper.writeValueAsString(Map.of())).isEqualTo("{}");
        assertThat(objectMapper.writeValueAsString(new SampleUser(null, "secret")))
                .isEqualTo("{\"account\":null}");
    }

    @Test
    @DisplayName("readValue는 JSON을 지정한 Java 타입으로 역직렬화한다")
    void deserializeObject() throws Exception {
        final var user = objectMapper.readValue("""
                {"account": "마이찬"}
                """, UserData.class);

        assertThat(user.account()).isEqualTo("마이찬");
    }

    public static class SampleUser {
        private final String account;
        private final String password;

        public SampleUser(final String account, final String password) {
            this.account = account;
            this.password = password;
        }

        public String getAccount() {
            return account;
        }

        @Override
        public String toString() {
            return "This is not JSON";
        }
    }

    public static class HiddenUser {
        private final String account = "마이찬";
    }

    public record UserData(String account) {
    }
}
