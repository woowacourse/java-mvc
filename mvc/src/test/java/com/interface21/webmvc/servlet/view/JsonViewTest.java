package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Test
    void 데이터가_1개인_모델은_값을_그대로_반환한다() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        Map<String, ?> model = Map.of("user", new TestUser("username", "username@gmail.com"));

        JsonNode expected = mapper.readTree("""
            {
                "account": "username",
                "email": "username@gmail.com"
            }
            """);

        // when
        new JsonView().render(model, request, response);
        JsonNode actual = mapper.readTree(body.toString());

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 데이터가_2개_이상이면_Map형태_그대로_JSON으로_변환해서_반환한다() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        Map<String, ?> model = Map.of(
            "user1", new TestUser("username1", "username1@gmail.com"),
            "user2", new TestUser("username2", "username2@gmail.com"));

        JsonNode expected = mapper.readTree("""
            {
                "user1": {
                    "account": "username1",
                    "email": "username1@gmail.com"
                },
                "user2": {
                    "account": "username2",
                    "email": "username2@gmail.com"
                }
            }
            """);

        // when
        new JsonView().render(model, request, response);
        JsonNode actual = mapper.readTree(body.toString());

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void Content_Type은_UTF_8_으로_응답한다() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        // when
        new JsonView().render(Map.of(), request, response);

        // then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}

record TestUser(String account, String email) {}
