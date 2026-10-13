package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JsonViewTest {

    private final JsonView view = new JsonView();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void singleObjectIsRenderedWithoutModelKey() throws Exception {
        final var response = render(Map.of("user", new TestUser("시오", "sio@example.com")));

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(response.getCharacterEncoding()).isEqualTo("UTF-8");
        assertThat(objectMapper.readTree(response.getContentAsString()))
                .isEqualTo(objectMapper.readTree("{\"account\":\"시오\",\"email\":\"sio@example.com\"}"));
    }

    @Test
    void multipleEntriesKeepTheirModelKeys() throws Exception {
        final var model = Map.of("users", List.of("시오", "gugu"), "count", 2);

        assertThat(objectMapper.readTree(render(model).getContentAsString()))
                .isEqualTo(objectMapper.valueToTree(model));
    }

    @Test
    void emptyModelIsRenderedAsEmptyObject() throws Exception {
        assertThat(render(Map.of()).getContentAsString()).isEqualTo("{}");
    }

    @Test
    void singleNullValueIsRenderedAsNull() throws Exception {
        final var model = new HashMap<String, Object>();
        model.put("user", null);

        assertThat(render(model).getContentAsString()).isEqualTo("null");
    }

    @Test
    void singleCollectionIsRenderedAsArray() throws Exception {
        assertThat(render(Map.of("users", List.of("sio", "gugu"))).getContentAsString())
                .isEqualTo("[\"sio\",\"gugu\"]");
    }

    private MockHttpServletResponse render(final Map<String, ?> model) throws Exception {
        final var response = new MockHttpServletResponse();
        view.render(model, new MockHttpServletRequest(), response);
        return response;
    }

    public record TestUser(String account, String email) {
    }
}
