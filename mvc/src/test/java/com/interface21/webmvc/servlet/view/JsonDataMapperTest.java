package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JsonDataMapperTest {

    @Test
    void 데이터가_1개면_값_그대로_반환한다() throws JsonProcessingException {
        JsonDataMapper jsonDataMapper = new JsonDataMapper();
        Map<String, String> model = Map.of("account", "jojo");

        String actual = jsonDataMapper.getData(model);
        String expected = new ObjectMapper().writeValueAsString("jojo");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 데이터가_2개_이상이면_JSON으로_반환한다() throws JsonProcessingException {
        JsonDataMapper jsonDataMapper = new JsonDataMapper();
        Map<String, String> model = Map.of("account", "jojo", "password", "1234");

        String actual = jsonDataMapper.getData(model);
        String expected = new ObjectMapper().writeValueAsString(model);

        assertThat(actual).isEqualTo(expected);
    }
}
