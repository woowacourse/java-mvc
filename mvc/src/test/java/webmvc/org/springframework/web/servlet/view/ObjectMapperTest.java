package webmvc.org.springframework.web.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ObjectMapperTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void writeValueAsString_1_entry_map() throws Exception {
        // given
        Map<String, Object> map = new HashMap<>();
        Member member = new Member("glen");
        map.put("member", member);

        // when
        String json = objectMapper.writeValueAsString(map);

        // then
        assertThat(json).isEqualTo("{\"member\":{\"name\":\"glen\"}}");
    }

    @Test
    void writeValueAsString_empty_map() throws Exception {
        // given
        Map<String, Object> map = new LinkedHashMap<>();

        // when
        String json = objectMapper.writeValueAsString(map);

        // then
        assertThat(json).isEqualTo("{}");
    }

    @Test
    void writeValueAsString_map() throws Exception {
        // given
        Map<String, Object> map = new LinkedHashMap<>();
        Member member = new Member("glen");
        map.put("member1", member);
        map.put("member2", member);

        // when
        String json = objectMapper.writeValueAsString(map);

        // then
        assertThat(json).isEqualTo("{\"member1\":{\"name\":\"glen\"},\"member2\":{\"name\":\"glen\"}}");
    }

    @Test
    void writeValueAsString_object() throws Exception {
        // given
        Member member = new Member("glen");

        // when
        String json = objectMapper.writeValueAsString(member);

        // then
        assertThat(json).isEqualTo("{\"name\":\"glen\"}");
    }

    private static class Member {
        String name;

        public Member(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
