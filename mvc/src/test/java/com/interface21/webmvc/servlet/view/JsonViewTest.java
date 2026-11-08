package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonViewTest {

    @Test
    void setJsonContentType() throws Exception{
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();

        new JsonView().render(Map.of("account", "gugu"), request, response);

        assertThat(response.getContentType())
                .isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void writesSingleModelValueAsJson() throws Exception {
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        var model = Map.of("user", Map.of("account", "gugu"));

        new JsonView().render(model, request, response);

        assertThat(response.getContentAsString())
                .isEqualTo("{\"account\":\"gugu\"}");
    }

    @Test
    void writesMultipleModelEntriesAsJson() throws Exception {
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        var model = Map.of("account", "gugu", "role", "member");

        new JsonView().render(model, request, response);

        var mapper = new ObjectMapper();

        assertThat(mapper.readTree(response.getContentAsString()))
                .isEqualTo(mapper.readTree("{\"account\":\"gugu\",\"role\":\"member\"}"));
    }
}
