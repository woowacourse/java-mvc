package nextstep.mvc.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.DummyUser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JsonConverterTest {

    @DisplayName("싱글턴으로 관리한다.")
    @Test
    void getInstance() {
        JsonConverter jsonConverter = JsonConverter.getInstance();
        assertThat(jsonConverter).isSameAs(JsonConverter.getInstance());
    }

    @DisplayName("객체를 JSON 문자열로 변환한다.")
    @Test
    void convertJson() {
        // given
        String expect =
                "{\"id\":1,\"account\":\"ggyool\",\"password\":\"password\",\"email\":\"ggyool@email.com\"}";
        DummyUser user = DummyUser.create();
        JsonConverter jsonConverter = JsonConverter.getInstance();

        // when, then
        assertThat(jsonConverter.convert(user)).isEqualTo(expect);
    }

    @DisplayName("키-값이 하나 있는 맵 객체를 JSON 문자열로 변환한다. (키 값이 없는 JSON 형태로 변환)")
    @Test
    void convertJsonFromSingleEntryMap() {
        // given
        String expect =
                "{\"id\":1,\"account\":\"ggyool\",\"password\":\"password\",\"email\":\"ggyool@email.com\"}";
        DummyUser user = DummyUser.create();
        Map<String, Object> map = Collections.singletonMap("user", user);
        JsonConverter jsonConverter = JsonConverter.getInstance();

        // when, then
        assertThat(jsonConverter.convert(map)).isEqualTo(expect);
    }

    @DisplayName("키-값이 여러 개 있는 맵 객체를 JSON 문자열로 변환한다.")
    @Test
    void convertJsonFromMap() {
        // given
        String userExpect =
                "\"user\":{\"id\":1,\"account\":\"ggyool\",\"password\":\"password\",\"email\":\"ggyool@email.com\"}";
        String intExpect = "\"int\":12345";

        DummyUser user = DummyUser.create();
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("int", 12345);
        JsonConverter jsonConverter = JsonConverter.getInstance();

        // when
        String json = jsonConverter.convert(map);

        // then
        assertThat(json.startsWith("{")).isTrue();
        assertThat(json.endsWith("}")).isTrue();
        assertThat(json).contains(userExpect);
        assertThat(json).contains(intExpect);
    }
}
