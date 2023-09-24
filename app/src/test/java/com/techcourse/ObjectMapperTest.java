package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ObjectMapperTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void objectMapper를_통해서_json_타입으로_변환_가능() {
        // given
        Map<String, Object> data = new HashMap<>();
        data.put("data1", "key1");
        data.put("data2", new JunHo());
        JsonNode jsonNode = objectMapper.valueToTree(data);
        String expected = "{\n"
            + "  \"data2\" : {\n"
            + "    \"names\" : [ \"junho\", \"junho2\" ],\n"
            + "    \"age\" : 100\n"
            + "  },\n"
            + "  \"data1\" : \"key1\"\n"
            + "}";

        // when
        String actual = jsonNode.toPrettyString();

        // then
        assertThat(expected).isEqualTo(actual);
    }

    private class JunHo {

        private List<String> names = List.of("junho", "junho2");
        private Integer age = 100;

        public List<String> getNames() {
            return names;
        }

        public Integer getAge() {
            return age;
        }
    }
}
