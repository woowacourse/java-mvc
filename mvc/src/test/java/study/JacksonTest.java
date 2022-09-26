package study;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JacksonTest {

    @Test
    void objectToJSON() throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Guardian yaho = new Guardian("yaho", 24);
        String value = objectMapper.writeValueAsString(yaho);

        assertAll(
                () -> assertThat(value.startsWith("{")).isTrue(),
                () -> assertThat(value.endsWith("}")).isTrue(),
                () -> assertThat(value.contains("\"name\":\"yaho\"")).isTrue(),
                () -> assertThat(value.contains("\"age\":24")).isTrue()
        );
    }

    @Test
    void objectToJSON_map_size1() throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Guardian yaho = new Guardian("yaho", 24);
        final Map<String, ?> model = Map.of("guardian", yaho);
        final String value = objectMapper.writeValueAsString(model.values());

        assertAll(
                () -> assertThat(value.startsWith("{")).isTrue(),
                () -> assertThat(value.endsWith("}")).isTrue(),
                () -> assertThat(value.contains("\"name\":\"yaho\"")).isTrue(),
                () -> assertThat(value.contains("\"age\":24")).isTrue()
        );

    }

    @Test
    void objectToJSON_map_size2() throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Guardian yaho = new Guardian("yaho", 24);
        final Pet yokie = new Pet("yokie", 13);
        final Map<String, ?> model = Map.of("guardian", yaho, "pet", yokie);
        final String value = objectMapper.writeValueAsString(model);

        assertAll(
                () -> assertThat(value.startsWith("{")).isTrue(),
                () -> assertThat(value.endsWith("}")).isTrue(),
                () -> assertThat(value.contains("\"guardian\":{\"name\":\"yaho\",\"age\":24}")).isTrue(),
                () -> assertThat(value.contains("\"pet\":{\"name\":\"yokie\",\"age\":13}")).isTrue()
        );
    }
}

class Guardian {

    private final String name;
    private final int age;

    Guardian(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}

class Pet {

    private final String name;
    private final int age;

    Pet(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
