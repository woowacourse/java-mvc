package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class JsonViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;

    private PrintWriter writer;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = mock(PrintWriter.class);
    }

    @Test
    void renderJsonTest() throws Exception {
        Map<String, Object> model = Map.of("member", new Member(), "team", "woowatech");
        View view = new JsonView();

        when(response.getWriter()).thenReturn(writer);
        view.render(model, request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(writer).write(captor.capture());

        String actual = captor.getValue();
        String expected = """
                        {
                            "member":{
                                "name":"jazz",
                                "age":26,
                                "tech":["java","MySQL","Spring"]},
                            "team":"woowatech"
                        }
                """;

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualNode = mapper.readTree(actual);
        JsonNode expectedNode = mapper.readTree(expected);

        assertThat(actualNode).isEqualTo(expectedNode);
    }


    static class Member {

        private String name = "jazz";
        private int age = 26;
        private List<String> tech = List.of("java", "MySQL", "Spring");

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public List<String> getTech() {
            return tech;
        }
    }
}
