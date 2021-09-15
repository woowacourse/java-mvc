package jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JacksonTest {

    final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void javaObjToJson() throws JsonProcessingException {
        final Car car = new Car("yellow", "bmw");
        final String jsonConverted = objectMapper.writeValueAsString(car);
        String jsonExpected = "{\"color\":\"yellow\",\"type\":\"bmw\"}";

        assertThat(jsonConverted).isEqualTo(jsonExpected);
    }

    @Test
    void jsonToJavaObj() throws JsonProcessingException {
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        final Car car = objectMapper.readValue(json, Car.class);

        assertThat(car.getColor()).isEqualTo("Black");
        assertThat(car.getType()).isEqualTo("BMW");
    }

    @Test
    void jsonToJsonNode() throws JsonProcessingException {
        String json = "{ \"color\" : \"Black\", \"type\" : \"FIAT\" }";
        final JsonNode jsonNode = objectMapper.readTree(json);

        final String color = jsonNode.get("color").asText();
        final String type = jsonNode.get("type").asText();
        assertThat(color).isEqualTo("Black");
        assertThat(type).isEqualTo("FIAT");
    }

    @Test
    void jsonToJavaList() throws JsonProcessingException {
        String jsonCarArray =
                "[" +
                    "{ \"color\" : \"Black\", \"type\" : \"BMW\" }, " +
                    "{ \"color\" : \"Red\", \"type\" : \"FIAT\" }" +
                "]";
        final List<Car> cars = objectMapper.readValue(jsonCarArray, new TypeReference<List<Car>>(){});

        assertThat(cars.get(0).getColor()).isEqualTo("Black");
        assertThat(cars.get(0).getType()).isEqualTo("BMW");
        assertThat(cars.get(1).getColor()).isEqualTo("Red");
        assertThat(cars.get(1).getType()).isEqualTo("FIAT");
    }
}
