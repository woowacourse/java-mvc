package nextstep.mvc.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonParserUtilsTest {

    @DisplayName("Map의 값을 Json으로 변환한다.")
    @Test
    void toJson() throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("account", "ecsimsw");
        map.put("name", "corgi");

        String json = JsonParserUtils.toJson(map);
        System.out.println(json);
    }
}