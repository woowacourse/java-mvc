package nextstep.mvc.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JsonModelMapperTest {

    @DisplayName("모델의 값이 하나인 경우의 응답을 확인한다")
    @Test
    void parseWithSingleValue() {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("key", "value");

        Model model = new Model(modelMap);

        String result = JsonModelMapper.parse(model);
        String expected = "value";

        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("모델의 값이 여러개인 경우의 응답을 확인한다")
    @Test
    void parseWithValues() {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("key1", "value1");
        modelMap.put("key2", "value2");

        Model model = new Model(modelMap);

        String result = JsonModelMapper.parse(model);
        String expected = "{\n" +
                "  \"key1\" : \"value1\",\n" +
                "  \"key2\" : \"value2\"\n" +
                "}";
        assertThat(result).isEqualTo(expected);
    }
}
