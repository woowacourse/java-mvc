package com.interface21.webmvc.servlet.mvc.tobe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class ObjectMapperTest {

    @Test
    void testGround() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> model = new HashMap<>();
        model.put("name", "changuii");
        model.put("city", "daegu");
        model.put("age", 14);

        String result = objectMapper.writeValueAsString(model);

        System.out.println(result);
    }
}
