package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonViewTest {

    @Test
    void usesDefaultMapperWhenNoMapperIsProvided() throws Exception {
        final var json = render(new JsonView(), Map.of("user", new Person("kim")));

        assertThat(json).isEqualTo("{\"firstName\":\"kim\"}");
    }

    @Test
    void appliesInjectedMapperSettingsToSingleModelValue() throws Exception {
        final var mapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        final var json = render(new JsonView(mapper), Map.of("user", new Person("kim")));

        assertThat(json).isEqualTo("{\"first_name\":\"kim\"}");
    }

    @Test
    void appliesInjectedMapperSettingsToMultipleModelValues() throws Exception {
        final var mapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        final Map<String, Object> model = new LinkedHashMap<>();
        model.put("user", new Person("kim"));
        model.put("count", 1);

        final var json = render(new JsonView(mapper), model);

        assertThat(json).isEqualTo("{\"user\":{\"first_name\":\"kim\"},\"count\":1}");
    }

    private String render(final JsonView view, final Map<String, ?> model) throws Exception {
        final var output = new StringWriter();
        final var response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(output));

        view.render(model, mock(HttpServletRequest.class), response);

        return output.toString();
    }

    public record Person(String firstName) {
    }
}
